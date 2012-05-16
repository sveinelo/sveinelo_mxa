package no.mxa.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.UniversalConstants;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MessageServiceTest extends SpringBasedTest {

	@Inject
	private MessageService messageService;
	@Inject
	private DataSource dataSource;
	private SimpleJdbcTemplate template;
	private Long existingId = 41L;

	/**
	 * Initial setup. Requires data in message table
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyAllDomainTables();
		template = new SimpleJdbcTemplate(dataSource);
	}

	/**
	 * Test find deviations. Requires data in message table
	 */
	@Test
	public void searchByMessageDeviationsTest() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 30,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", null);

		// Find all messages which have
		// 1) Failed in sending to Altinn
		// OR
		// 2) Not been read within read deadline
		Date date = new Date(System.currentTimeMillis());
		List<MessageDTO> messagesWithDeviations = messageService.searchByMessageDeviations(date);

		int numberOfMessages = messagesWithDeviations.size();
		assertThat(numberOfMessages, is(1));
	}

	/**
	 * Test if given message has deviation. Requires data in message table
	 */
	@Test
	public void hasDeviationTest() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 30,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", null);

		Date futureDate = new Date(System.currentTimeMillis() + 1000000L);
		List<MessageDTO> messagesWithDeviation = messageService.searchByProperty("messageStatus", 30);

		assertThat(messagesWithDeviation.size(), is(1));
		boolean returnValueDeviation = messageService.hasDeviation(messagesWithDeviation.get(0).getMessageKey());
		assertThat(returnValueDeviation, is(true));

		messagesWithDeviation.get(0).setMessageStatus(10);
		messagesWithDeviation.get(0).setReadDeadline(futureDate);
		boolean returnValueOK = messageService.hasDeviation(messagesWithDeviation.get(0).getMessageKey());
		assertThat(returnValueOK, is(false));
	}

	/**
	 * Test search by messageKey. Requires data in message table
	 */
	@Test
	public void searchByMessageKeyTest() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 30,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", null);

		// Get MessageDTO
		MessageDTO message = messageService.searchByProperty("sentAltinn", 1).get(0);

		assertThat(message, is(notNullValue()));

		String inputMessageKey = message.getMessageKey();
		MessageDTO matchingMessage = messageService.searchByMessageKey(inputMessageKey);

		// Assert the method returns one matching message
		assertThat(matchingMessage, is(notNullValue()));
		assertThat(matchingMessage, is(message));
	}

	/**
	 * Test status updates related to re-sending. Requires data in message table
	 */
	@Test
	public void updateMessageStatusAndSentAltinnFlagTest() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 30,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", null);

		// Messages with sentAltinn = '1' is OK to re-send
		MessageDTO inputMessage = new MessageDTO();
		inputMessage.setSentAltinn(1);
		List<MessageDTO> messagesToUpdate = messageService.searchByCriteria(inputMessage);

		assertThat(messagesToUpdate.size(), is(1));

		Long messageId = messagesToUpdate.get(0).getId();
		int oldNumberOfLogEntries = messagesToUpdate.get(0).getLog().size();

		// Update message and save it
		messageService.updateMessageStatusAndSentAltinnFlag(messageId);

		// Check saved message
		MessageDTO updatedMessage = messageService.searchById(messageId);
		int newNumberOfLogEntries = updatedMessage.getLog().size();

		assertThat(updatedMessage, is(notNullValue()));
		assertThat(oldNumberOfLogEntries < newNumberOfLogEntries, is(true));

		// Messages with sentAltinn = '0' is not allowed to re-send
		inputMessage.setSentAltinn(0);
		messagesToUpdate = this.messageService.searchByCriteria(inputMessage);

		assertThat(messagesToUpdate.size(), is(1));

		messageId = messagesToUpdate.get(0).getId();
		oldNumberOfLogEntries = messagesToUpdate.get(0).getLog().size();

		// Update message and save it
		messageService.updateMessageStatusAndSentAltinnFlag(messageId);

		// Check saved message
		updatedMessage = messageService.searchById(messageId);
		newNumberOfLogEntries = updatedMessage.getLog().size();

		assertThat(updatedMessage, is(notNullValue()));

		// The message is not updated, so the number of old log entries and
		// new log entries are asserted equal
		assertThat(oldNumberOfLogEntries, is(newNumberOfLogEntries));
	}

	/**
	 * Test status updates related to manual deviation removal. Requires data in message table
	 */
	@Test
	public void updateMessageStatusTest() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 30,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", null);

		// Messages with messageStatus = 30 has deviations
		MessageDTO inputMessage = new MessageDTO();
		inputMessage.setMessageStatus(30);
		List<MessageDTO> messagesToUpdate = messageService.searchByCriteria(inputMessage);

		assertThat(messagesToUpdate.size(), is(1));

		Long messageId = messagesToUpdate.get(0).getId();
		int oldNumberOfLogEntries = messagesToUpdate.get(0).getLog().size();
		int oldMessageStatus = messagesToUpdate.get(0).getMessageStatus();

		// Update message and save it
		this.messageService.updateMessageStatus(messageId);

		// Check saved message
		MessageDTO updatedMessage = messageService.searchById(messageId);
		int newNumberOfLogEntries = updatedMessage.getLog().size();
		int newMessageStatus = updatedMessage.getMessageStatus();

		assertThat(updatedMessage, is(notNullValue()));

		assertThat(oldNumberOfLogEntries < newNumberOfLogEntries, is(true));
		assertThat(oldMessageStatus, not(is(newMessageStatus)));
	}

	/**
	 * Test advanced search options. Requires data in message table
	 */
	@Test
	public void searchFromGuiTest() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 30,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", null);

		List<MessageDTO> returnList = null;

		// Test %LIKE% search on message reference
		MessageDTO criteria = new MessageDTO();

		returnList = messageService.searchFromGUI(criteria, null, null, null, "MSREF");
		assertThat(returnList.get(0), is(notNullValue()));

		// Test %LIKE% on caseDescription
		returnList = messageService.searchFromGUI(criteria, null, null, "CaseDesc", null);
		assertThat(returnList.get(0), is(notNullValue()));

		// Test exact match in combination with %LIKE%
		criteria.setAltinnArchive("Altinn");
		returnList = messageService.searchFromGUI(criteria, null, null, "CaseDesc", "MSREF");
		assertThat(returnList.get(0), is(notNullValue()));
	}

	@Test
	public void searchFromGuiFromDate() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 10,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.minusDays(5).toDate());
		insertTestdata(existingId + 1L, "Key002", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1,
				10, dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.minusDays(7).toDate());

		MessageDTO criteria = new MessageDTO();
		List<MessageDTO> returnList = null;

		Date fromDate = dateTime.minusDays(6).toDate();
		returnList = messageService.searchFromGUI(criteria, fromDate, null, null, null);
		assertThat(returnList.size(), is(1));
		assertThat(returnList.get(0), is(notNullValue()));
		assertThat(returnList.get(0).getId(), is(existingId));
	}

	@Test
	public void searchFromGuiToDate() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 10,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.plusDays(5).toDate());
		insertTestdata(existingId + 1L, "Key002", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1,
				10, dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.plusDays(7).toDate());

		MessageDTO criteria = new MessageDTO();
		List<MessageDTO> returnList = null;

		Date toDate = dateTime.plusDays(6).toDate();
		returnList = messageService.searchFromGUI(criteria, null, toDate, null, null);
		assertThat(returnList.size(), is(1));
		assertThat(returnList.get(0), is(notNullValue()));
		assertThat(returnList.get(0).getId(), is(existingId));
	}

	@Test
	public void searchFromGuiBetweenDates() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 10,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.minusDays(5).toDate());
		insertTestdata(existingId + 1L, "Key002", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1,
				10, dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.plusDays(5).toDate());
		insertTestdata(existingId + 2L, "Key002", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1,
				10, dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.plusDays(6).toDate());

		MessageDTO criteria = new MessageDTO();
		List<MessageDTO> returnList = null;

		Date fromDate = dateTime.minusDays(6).toDate();
		Date toDate = dateTime.plusDays(4).toDate();
		returnList = messageService.searchFromGUI(criteria, fromDate, toDate, null, null);
		assertThat(returnList.size(), is(2));
	}

	@Test
	public void searchFromGuiToDateCmbinedWithCaseDescription() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 10,
				dateTime.toDate(), 0, "CaseDesc1", "Altinn", dateTime.plusDays(5).toDate());
		insertTestdata(existingId + 1L, "Key002", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1,
				10, dateTime.toDate(), 0, "CaseDesc2", "Altinn", dateTime.plusDays(5).toDate());

		MessageDTO criteria = new MessageDTO();
		List<MessageDTO> returnList = null;

		Date toDate = dateTime.plusDays(6).toDate();
		returnList = messageService.searchFromGUI(criteria, null, toDate, "CaseDesc1", null);
		assertThat(returnList.size(), is(1));
		assertThat(returnList.get(0), is(notNullValue()));
		assertThat(returnList.get(0).getId(), is(existingId));
	}

	@Test
	public void searchFromGuiToDateCmbinedWithMessageReference() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF1", "Proc", "Header", "Summary", 1, 10,
				dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.plusDays(5).toDate());
		insertTestdata(existingId + 1L, "Key002", "AGENCY", 0, "PS", "01010101010", "MSREF2", "Proc", "Header", "Summary", 1,
				10, dateTime.toDate(), 0, "CaseDesc", "Altinn", dateTime.plusDays(5).toDate());

		MessageDTO criteria = new MessageDTO();
		List<MessageDTO> returnList = null;

		Date toDate = dateTime.plusDays(6).toDate();
		returnList = messageService.searchFromGUI(criteria, null, toDate, null, "MSREF1");
		assertThat(returnList.size(), is(1));
		assertThat(returnList.get(0), is(notNullValue()));
		assertThat(returnList.get(0).getId(), is(existingId));
	}

	/**
	 * Test search for notification messages. Requires data in message table
	 */
	@Test
	public void searchForNewNotificationMessages() {
		DateTime dateTime = new DateTime();

		insertTestdata(existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header", "Summary", 1, 10,
				dateTime.plusDays(-5).toDate(), 0, "CaseDesc", "Altinn", null);

		List<MessageDTO> messageList = messageService.searchNoticeMessages();
		assertThat(messageList.size(), is(1));
		assertThat(messageList.get(0), is(notNullValue()));
		MessageDTO message = messageList.get(0);

		assertThat(message.getOverdueNoticeSent(), is(UniversalConstants.MSG_OVERDUENOTICE_FALSE));
		assertThat(message.getReadDeadline().before(new Date()), is(true));
		assertThat(message.getMessageStatus() < new Integer(41), is(true));
		assertThat(message.getMessageStatus() != UniversalConstants.MSG_STATUS_SEND_ALTINN_FAILED, is(true));
		assertThat(message.getMessageStatus() != UniversalConstants.MSG_STATUS_READ_IN_ALTINN, is(true));
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	private void insertTestdata(Object... args) {
		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT, CASEDESCRIPTION, ALTINNARCHIVE, SENTALTINNDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?)";

		template.update(messageQuery, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9],
				args[10], args[11], args[12], args[13], args[14], args[15], args[16]);
	}

}
