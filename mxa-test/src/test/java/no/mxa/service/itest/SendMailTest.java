/*
 * #%L
 * Integration tests
 * %%
 * Copyright (C) 2009 - 2012 Patentstyret
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
package no.mxa.service.itest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.sql.DataSource;

import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.MessageDTO;
import no.mxa.service.KeyValues;
import no.mxa.service.RecipientDTO;
import no.mxa.service.SendMailService;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SendMailTest extends SpringBasedTest {
	@Inject
	private SendMailService sendMailService;
	@Inject
	private MessageRepository repository;
	@Inject
	private KeyValues keyValues;
	@Inject
	private DataSource dataSource;
	private Long existingId = 78L;

	/**
	 * Initial setup. Requires data in message table
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);
		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, existingId, "MAILNOTICESUBJECT", null, null, "Subject", "Description");
		template.update(keyvaluesQuery, existingId, "MAILNOTICECONTENT", null, null, "Content", "Description");
		template.update(keyvaluesQuery, existingId, "MAILFROM", null, null, "mxa@mxa.no", "Description");
		template.update(keyvaluesQuery, existingId, "SMTPHOST", null, null, "0.0.0.0", "Description");
		template.update(keyvaluesQuery, existingId, "SMTPUSER", null, null, "testuser", "Description");
		template.update(keyvaluesQuery, existingId, "SMTPPASSWORD", null, null, "testpassword", "Description");
	}

	@Test
	public void testSendMailMessageWithAttatcments() throws MessagingException, SQLException {
		List<RecipientDTO> recipients = new ArrayList<RecipientDTO>();
		recipients.add(new RecipientDTO(RecipientType.TO, new InternetAddress("mxa@mxa.no")));
		recipients.add(new RecipientDTO(RecipientType.CC, new InternetAddress("zerodev@me.com")));
		MessageDTO message = repository.findById(existingId);
		sendMailService.sendMailMessage(recipients, keyValues.getMailNoticeSubject(), keyValues.getMailNoticeContent(),
				message.getAttachments(), keyValues.getMailFrom());
	}

}
