package no.mxa.hibernate.mapping.itest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.OracleBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test Oracle database.
 * 
 */
@Transactional
@DirtiesContext
public class OracleIntegrationTest extends OracleBasedTest {

	@Inject
	private DataSource dataSource;
	private SimpleJdbcTemplate template;
	private final long messageId = 200L;
	private MessageRepository repository;

	@Before
	public void prepareDataBase() {
		template = new SimpleJdbcTemplate(dataSource);

		template.update("DELETE FROM MESSAGE where id = " + messageId);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, messageId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);
	}

	@Test
	public void shouldRetrieveDataFromOracleDatabase() {
		MessageDTO result = repository.findById(messageId);
		assertThat(result, is(notNullValue()));
		assertThat(result.getMessageKey(), is("Key001"));
	}

	@Inject
	public void setRepository(MessageRepository repository) {
		this.repository = repository;
	}

}
