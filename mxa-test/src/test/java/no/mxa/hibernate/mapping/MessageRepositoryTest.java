package no.mxa.hibernate.mapping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The tests in this class were used during Hibernate configuration.
 */
@Transactional
public class MessageRepositoryTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	private MessageRepository repository;
	private Long existingId = 32L;

	@Inject
	public void setRepository(MessageRepository repository) {
		this.repository = repository;
	}

	/**
	 * Initial setup. Requires data in message table
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);
	}

	/**
	 * Test findById. Requires data in message table
	 */
	@Test
	public void testFindById() {
		MessageDTO result = repository.findById(existingId);
		assertThat(result, is(notNullValue()));
		assertThat(result.getMessageKey(), is("Key001"));
	}

	/**
	 * Test save
	 */
	@Test
	public void testSave() {
		MessageDTO instance = repository.findById(existingId);
		assertThat(instance, is(notNullValue()));
		instance.setDomain("DOMAIN");
		repository.save(instance);

		MessageDTO updatedInstance = repository.findById(existingId);
		assertThat(updatedInstance, is(notNullValue()));
		assertThat(updatedInstance.getDomain(), is("DOMAIN"));
	}

	/**
	 * Test findByProperty. Requires data in message table
	 */
	@Test
	public void testFindByProperty() {
		List<MessageDTO> messageList = repository.findByProperty("domain", "PS");
		assertThat(messageList.get(0), is(notNullValue()));
	}

	/**
	 * Test findByExample. Requires data in message table
	 */
	@Test
	public void testFindByStringExample() {
		MessageDTO criteria = new MessageDTO();
		criteria.setDomain("PS");
		criteria.setSendingSystem("AGENCY");

		List<MessageDTO> messageList = repository.findByExample(criteria);
		assertThat(messageList.get(0), is(notNullValue()));
	}

	/**
	 * Test findByExample with integer input. Requires data in message table
	 */
	@Test
	public void testFindByIntegerExample() {
		MessageDTO criteria = new MessageDTO();
		criteria.setMessageStatus(10);

		List<MessageDTO> messageList = repository.findByExample(criteria);
		assertThat(messageList.get(0), is(notNullValue()));
	}

}