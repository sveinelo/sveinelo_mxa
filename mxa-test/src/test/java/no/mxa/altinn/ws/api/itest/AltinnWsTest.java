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
package no.mxa.altinn.ws.api.itest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.altinn.ws.AltinnFault;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasic;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicTestAltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.altinn.ws.ReceiptStatusEnum;
import no.mxa.altinn.ws.api.AltinnWS;
import no.mxa.altinn.ws.api.CorrespondenceBuilderException;
import no.mxa.dto.AttachmentDTO;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class AltinnWsTest extends SpringBasedTest {
	private static final String UNUSED_DESCRIPTION = "Description";

	@Inject
	private AltinnWS altinnWS;
	@Inject
	private ICorrespondenceAgencyExternalBasic port;

	@Before
	public void setup() {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = getSimpleJdbcTemplate();
		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, 1L, "SYSTEMUSERNAME", null, null, "PAT", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 2L, "SYSTEMUSERCODE", null, null, "PAT", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 3L, "LANGUAGECODE", null, null, "1044", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 4L, "ALTINNPASSWORD", null, null, "Wrong Password", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 5L, "SERVICECODE", null, null, "PAT", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 6L, "SERVICEEDITION", null, null, "1", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 7L, "MAILFROM", null, null, "mxa@mxa.tst", UNUSED_DESCRIPTION);
		template.update(keyvaluesQuery, 8L, "NOTIFICATIONTYPE", null, null, "Correspondence", UNUSED_DESCRIPTION);
	}

	@Test
	public void pingConfiguredEndpoint() throws ICorrespondenceAgencyExternalBasicTestAltinnFaultFaultFaultMessage {
		port.test();
	}

	@Test
	public void shouldSendAMessageToAltinnTest() throws MalformedURLException, CorrespondenceBuilderException {
		MessageDTO message = createMessage();
		try {
			ReceiptExternal receiptExternal = altinnWS.sendMessage(message);
			assertThat(receiptExternal.getReceiptStatusCode(), is(ReceiptStatusEnum.OK));
		} catch (ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage e) {
			AltinnFault faultInfo = e.getFaultInfo();
			fail("This tests will fail with incorrect keyValues.\n Message from Altinn: "
					+ faultInfo.getAltinnLocalizedErrorMessage().getValue());
		}
	}

	private MessageDTO createMessage() {
		MessageDTO message = new MessageDTO();
		message.setMessageReference("REF001");
		message.setParticipantId("01234567890");
		message.setCaseOfficer("Test Case Officer");
		message.setCaseDescription("Test Case Description");
		message.setMessageHeader("AltinnWsTestHeader");
		message.setMessageSummary("Altinn Ws Test Summary in Altut or Body in Correspondence.");
		List<AttachmentDTO> attachments = createAttachemnts();
		message.setAttachments(attachments);
		List<ContactInfoDTO> contactInfos = createContactInfos(message);
		message.setContactInfo(contactInfos);

		message.setAltinnArchive("AltinnArchive");
		message.setBatchSending(0);
		message.setCaseDescription("caseDescription");
		message.setCaseOfficer("caseOfficer");
		message.setDomain("DOMAIN");
		message.setDueDate(new DateTime().plusDays(1).toDate());
		message.setIdproc("idproc");
		message.setMessageKey("messageKey");
		message.setMessageStatus(UniversalConstants.MSG_STATUS_RECEIVED);
		message.setOverdueNoticeSent(UniversalConstants.MSG_OVERDUENOTICE_FALSE);
		message.setReadDeadline(new DateTime().plusDays(20).toDate());
		message.setSendingSystem("AltinnWsTest");
		message.setSentAltinn(UniversalConstants.MSG_SENTALTINN_FALSE);
		message.setSentAltinnDate(null);
		return message;
	}

	private List<AttachmentDTO> createAttachemnts() {
		List<AttachmentDTO> attachments = new ArrayList<>();
		String string = "TVhBLWRva3VtZW50IHRpbCBBbHR1dC4K"; // Content "MXA-dokument til Altut."
		String mimeType = "application/txt";
		String fileName = "mxa_dokument_til_altinn_ii.txt";
		String name = "Visningsnavn for dokumentet, æøå.";
		String attachmentAsString = string;
		AttachmentDTO attachement = new AttachmentDTO(mimeType, fileName, name, attachmentAsString);
		attachments.add(attachement);
		return attachments;
	}

	/** Notifications */
	private List<ContactInfoDTO> createContactInfos(MessageDTO message) {
		List<ContactInfoDTO> contactInfos = new ArrayList<>();
		ContactInfoDTO contactInfo = new ContactInfoDTO();
		contactInfo.setType(UniversalConstants.CONTACTINFOTYPE_EMAIL);
		contactInfo.setAddress("Svein.Erik.Lovland@visma.com");
		contactInfo.setMessage(message);
		contactInfos.add(contactInfo);
		return contactInfos;
	}
}