package no.mxa.service.implementations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.UniversalConstants;
import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class LogGeneratorTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	@Inject
	private LogGenerator logGenerator;
	private LogDTO logDTO;
	MessageRepository messageRepository;
	private Long existingId = 58L;

	@Inject
	public void setRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Before
	public void setUp() throws Exception {
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);
	}

	@Test
	public void testGenerateLogStandalone() {
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.RCT_FTP_FAULT);

		assertThat(logDTO.getLogMessage(), is(notNullValue()));
	}

	@Test
	public void testGenerateLogWithMessageId() {
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.MSG_SENT_ALTINN, existingId);

		assertThat(logDTO.getMessage().getId(), is(existingId));
	}

	@Test
	public void testGenerateLogWithMessageIdAndTimestamp() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.MSG_SENT_ALTINN, existingId, now);

		assertThat(logDTO.getTime(), is(now));
	}

	@Test
	public void testGenerateLogWithMessageReference() {
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.MSG_SENT_ALTINN, existingId);
		MessageDTO message = messageRepository.findById(existingId);

		assertThat(logDTO.getMessage(), is(message));
	}

	@Test
	public void testSaveLog() {
		logDTO = logGenerator.generateLog(" Ekstra logg", "Oversendelse Altinn", existingId);
		logGenerator.saveLog(logDTO);
		Long savedId = logDTO.getId();

		assertThat(savedId, is(notNullValue()));
	}

}
