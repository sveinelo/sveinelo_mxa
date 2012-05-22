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
package no.mxa.service.batch.notification.itest;

import static no.mxa.UniversalConstants.MSG_OVERDUENOTICE_FALSE;
import static no.mxa.UniversalConstants.MSG_SENTALTINN_TRUE;
import static no.mxa.UniversalConstants.MSG_STATUS_SENT_ALTINN;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.sql.DataSource;

import no.mxa.dto.KeyValuesDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.batch.notification.SendNoticesAndWarnings;
import no.mxa.test.support.SpringBasedTest;
import no.mxa.test.support.TestDataHelper;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Depends on available SMTP host.")
public class SendDeviationNoticeAndWarnTest extends SpringBasedTest {
	@Inject
	private SendNoticesAndWarnings sendDeviationNoticeAndWarn;
	@Inject
	private DataSource dataSource;

	@Before
	public void setup() {
		emptyAllDomainTables();
		TestDataHelper dataHelper = new TestDataHelper(dataSource);

		MessageDTO m = new MessageDTO();
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

		dataHelper.insertMessage(m);
		Long keyvalueId = 0L;
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

	}

	@Test
	public void startSendDeviationNoticeAndWarn() throws MessagingException {
		sendDeviationNoticeAndWarn.start();
	}

}
