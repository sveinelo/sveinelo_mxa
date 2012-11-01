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
package no.mxa.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.FreePortUtil;
import no.mxa.test.support.SpringBasedTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class SendMailTest extends SpringBasedTest {
	private static final String SUBJECT = "Subject";
	private static final String MAIL_FROM = "mxa@mxa.no";
	@Inject
	private MessageRepository repository;
	@Inject
	private KeyValues keyValues;
	@Inject
	private DataSource dataSource;
	private Wiser wiser;
	@Inject
	private SendMailService sendMailService;

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
		template.update(messageQuery, 1L, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);
		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, 1L, "MAILNOTICESUBJECT", null, null, SUBJECT, "Description");
		template.update(keyvaluesQuery, 2L, "MAILNOTICECONTENT", null, null, "Content", "Description");
		template.update(keyvaluesQuery, 3L, "MAILFROM", null, null, MAIL_FROM, "Description");
		template.update(keyvaluesQuery, 4L, "SMTPHOST", null, null, "localhost", "Description");
		template.update(keyvaluesQuery, 5L, "SMTPUSER", null, null, null, "Description");
		template.update(keyvaluesQuery, 6L, "SMTPPASSWORD", null, null, null, "Description");
		int localPort = FreePortUtil.findPort();
		template.update(keyvaluesQuery, 7L, "SMTPPORT", null, localPort, null, "Description");

		wiser = new Wiser(localPort);
		wiser.setHostname("localhost");
		wiser.start();
	}

	@After
    public void after() {
        wiser.stop();
    }

	@Test
	@Transactional
	public void testSendMailMessageWithAttatcments() throws MessagingException, SQLException {
		List<RecipientDTO> recipients = new ArrayList<RecipientDTO>();
		recipients.add(new RecipientDTO(RecipientType.TO, new InternetAddress(MAIL_FROM)));
		recipients.add(new RecipientDTO(RecipientType.CC, new InternetAddress("zerodev@me.com")));
		MessageDTO message = repository.findById(1L);
		sendMailService.sendMailMessage(recipients, keyValues.getMailNoticeSubject(), keyValues.getMailNoticeContent(),
				message.getAttachments(), keyValues.getMailFrom());
		
		List<WiserMessage> messages = wiser.getMessages();
		assertThat(messages.size(), is(2));
		for (WiserMessage wiserMessage : messages) {
			assertThat(wiserMessage.getEnvelopeSender(), is(MAIL_FROM));
			MimeMessage mimeMessage = wiserMessage.getMimeMessage();
			assertThat(mimeMessage.getSubject(), is(SUBJECT));
		}
	}
}
