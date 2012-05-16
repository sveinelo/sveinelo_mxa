package no.mxa.test.support;

import javax.sql.DataSource;

import no.mxa.dto.KeyValuesDTO;
import no.mxa.dto.MessageDTO;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class TestDataHelper {
	private final String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";

	private final String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
			+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
			+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private SimpleJdbcTemplate template;

	public TestDataHelper(DataSource dataSource) {
		template = new SimpleJdbcTemplate(dataSource);
	}

	public void insertMessage(MessageDTO m) {
		template.update(messageQuery, m.getId(), m.getMessageKey(), m.getSendingSystem(), m.getBatchSending(), m.getDomain(),
				m.getParticipantId(), m.getMessageReference(), m.getIdproc(), m.getMessageHeader(), m.getMessageSummary(),
				m.getSentAltinn(), m.getMessageStatus(), m.getReadDeadline(), m.getOverdueNoticeSent());
	}

	public void insertKeyvalue(KeyValuesDTO kv) {
		template.update(keyvaluesQuery, kv.getId(), kv.getKey(), kv.getDateValue(), kv.getNumericValue(), kv.getStringValue(),
				kv.getDescription());
	}

}
