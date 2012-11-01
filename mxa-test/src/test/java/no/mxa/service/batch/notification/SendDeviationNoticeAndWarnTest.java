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
package no.mxa.service.batch.notification;

import static no.mxa.UniversalConstants.MSG_OVERDUENOTICE_FALSE;
import static no.mxa.UniversalConstants.MSG_SENTALTINN_TRUE;
import static no.mxa.UniversalConstants.MSG_STATUS_SENT_ALTINN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.sql.DataSource;

import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.KeyValuesDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.FreePortUtil;
import no.mxa.test.support.SpringBasedTest;
import no.mxa.test.support.TestDataHelper;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class SendDeviationNoticeAndWarnTest extends SpringBasedTest {
	@Inject
	private SendNoticesAndWarnings sendDeviationNoticeAndWarn;
	@Inject
	private DataSource dataSource;
	private Wiser wiser;

	@Before
	public void setup() throws IOException {
		emptyAllDomainTables();
		TestDataHelper dataHelper = new TestDataHelper(dataSource);
		Long keyvalueId = 0L;
		configureAndStartFakeSmtpServer(dataHelper, keyvalueId);
		insertAMessage(dataHelper);
		configureMailMessage(dataHelper, keyvalueId);
	}

	@After
	public void after() {
		wiser.stop();
	}

	@Test
	public void startSendDeviationNoticeAndWarn() throws MessagingException {
		sendDeviationNoticeAndWarn.start();
		List<WiserMessage> messages = wiser.getMessages();
		assertThat(messages.size(), is(2));
	}

	private void configureMailMessage(TestDataHelper dataHelper, Long keyvalueId) {
		KeyValuesDTO mailToSubject = new KeyValuesDTO();
		mailToSubject.setId(keyvalueId++);
		mailToSubject.setKey("MAILNOTICESUBJECT");
		mailToSubject.setStringValue("TEST Subject");

		dataHelper.insertKeyvalue(mailToSubject);

		KeyValuesDTO mailFrom = new KeyValuesDTO();
		mailFrom.setId(keyvalueId++);
		mailFrom.setKey("MAILFROM");
		mailFrom.setStringValue("mxa_test@mxa.no");

		dataHelper.insertKeyvalue(mailFrom);

		KeyValuesDTO mailNoticeContent = new KeyValuesDTO();
		mailNoticeContent.setId(keyvalueId++);
		mailNoticeContent.setKey("MAILNOTICECONTENT");
		mailNoticeContent.setStringValue("Test Message From automatic test");

		dataHelper.insertKeyvalue(mailNoticeContent);

		KeyValuesDTO mailNoticeDays = new KeyValuesDTO();
		mailNoticeDays.setId(keyvalueId++);
		mailNoticeDays.setKey("MAILNOTICEDAYS");
		mailNoticeDays.setNumericValue(Integer.valueOf(1));

		dataHelper.insertKeyvalue(mailNoticeDays);

		KeyValuesDTO mailWarnDays = new KeyValuesDTO();
		mailWarnDays.setId(keyvalueId++);
		mailWarnDays.setKey("MAILWARNDAYS");
		mailWarnDays.setNumericValue(Integer.valueOf(2));

		dataHelper.insertKeyvalue(mailWarnDays);

		KeyValuesDTO mailWarnSubject = new KeyValuesDTO();
		mailWarnSubject.setId(keyvalueId++);
		mailWarnSubject.setKey("MAILWARNSUBJECT");
		mailWarnSubject.setStringValue("WARNING");

		dataHelper.insertKeyvalue(mailWarnSubject);

		KeyValuesDTO mailWarnContent = new KeyValuesDTO();
		mailWarnContent.setId(keyvalueId++);
		mailWarnContent.setKey("MAILWARNCONTENT");
		mailWarnContent.setStringValue("WARNING CONTENT");

		dataHelper.insertKeyvalue(mailWarnContent);

		KeyValuesDTO mailToPat = new KeyValuesDTO();
		mailToPat.setId(keyvalueId++);
		mailToPat.setKey("MAILTOPAT");
		mailToPat.setStringValue("mail@to.pat");

		dataHelper.insertKeyvalue(mailToPat);
	}

	private void insertAMessage(TestDataHelper dataHelper) {
		MessageDTO m = new MessageDTO();
		long messageId = 1L;
		m.setId(messageId);
		m.setMessageKey("MessageKey");
		m.setSendingSystem("TEST");
		m.setBatchSending(-1);
		m.setDomain("TS");
		m.setParticipantId("01000000000");
		m.setMessageReference("TEST-REF");
		m.setIdproc("TEST-PROC");
		m.setMessageHeader("TEST HEADER");
		m.setMessageSummary("TEST SUMMARY");
		m.setSentAltinn(MSG_SENTALTINN_TRUE);
		m.setMessageStatus(MSG_STATUS_SENT_ALTINN);
		m.setReadDeadline(new DateTime().minusDays(8).toDate());
		m.setOverdueNoticeSent(MSG_OVERDUENOTICE_FALSE);
		List<ContactInfoDTO> contactInfo = new ArrayList<>();
		ContactInfoDTO contact = new ContactInfoDTO("EMAIL", "zerodev@me.com");
		contact.setId(messageId);
		contactInfo.add(contact);
		m.setContactInfo(contactInfo);

		dataHelper.insertMessage(m);
	}

	private void configureAndStartFakeSmtpServer(TestDataHelper dataHelper, Long keyvalueId) throws IOException {
		KeyValuesDTO smtpPort = new KeyValuesDTO();
		smtpPort.setId(keyvalueId++);
		smtpPort.setKey("SMTPPORT");
		int localPort = FreePortUtil.findPort();
		smtpPort.setNumericValue(Integer.valueOf(localPort));

		dataHelper.insertKeyvalue(smtpPort);

		wiser = new Wiser(localPort);
		wiser.setHostname("localhost");
		wiser.start();

		KeyValuesDTO smtpHost = new KeyValuesDTO();
		smtpHost.setId(keyvalueId++);
		smtpHost.setKey("SMTPHOST");
		smtpHost.setStringValue("localhost");

		dataHelper.insertKeyvalue(smtpHost);

		KeyValuesDTO smtpUser = new KeyValuesDTO();
		smtpUser.setId(keyvalueId++);
		smtpUser.setKey("SMTPUSER");
		smtpUser.setStringValue(null);

		dataHelper.insertKeyvalue(smtpUser);

		KeyValuesDTO smtpPassword = new KeyValuesDTO();
		smtpPassword.setId(keyvalueId++);
		smtpPassword.setKey("SMTPPASSWORD");
		smtpPassword.setStringValue(null);

		dataHelper.insertKeyvalue(smtpPassword);
	}
}
