/*
 * #%L
 * Integration tests
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package no.mxa.test.support;

import javax.sql.DataSource;

import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.KeyValuesDTO;
import no.mxa.dto.MessageDTO;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class TestDataHelper {
	private static final String contactQuery = "INSERT INTO CONTACTINFO (ID, MESSAGEID, TYPE, ADDRESS) VALUES (?, ?, ?, ?)";

	private static final String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";

	private static final String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
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

		for (ContactInfoDTO contact : m.getContactInfo()) {
			template.update(contactQuery, contact.getId(), m.getId(), contact.getType(), contact.getAddress());
		}
	}

	public void insertKeyvalue(KeyValuesDTO kv) {
		template.update(keyvaluesQuery, kv.getId(), kv.getKey(), kv.getDateValue(), kv.getNumericValue(), kv.getStringValue(),
				kv.getDescription());
	}

}
