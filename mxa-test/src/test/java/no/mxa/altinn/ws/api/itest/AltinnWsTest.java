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
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import no.mxa.altinn.ws.AltinnFault;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasic;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicTestAltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.altinn.ws.ReceiptStatusEnum;
import no.mxa.altinn.ws.api.AltinnWS;
import no.mxa.altinn.ws.api.CorrespondenceBuilderException;
import no.mxa.dto.AttachmentDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class AltinnWsTest extends SpringBasedTest {

	@Inject
	private AltinnWS altinnWS;
	@Inject
	private ICorrespondenceAgencyExternalBasic port;

	@Before
	public void setup() {
		SimpleJdbcTemplate template = getSimpleJdbcTemplate();
		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, 1L, "GOVORGAN", null, null, "PAT", "Description");
		template.update(keyvaluesQuery, 2L, "LANGUAGECODE", null, null, "1044", "Description");
		template.update(keyvaluesQuery, 3L, "ALTINNPASSWORD", null, null, "Wrong Password", "Description");
		template.update(keyvaluesQuery, 4L, "SERVICECODE", null, null, "PAT", "ServiceCode");
		template.update(keyvaluesQuery, 5L, "SERVICEEDITION", null, null, "1", "ServiceEdition");
	}

	@Test
	@Ignore("Need to fix test-setup")
	public void testName() throws ICorrespondenceAgencyExternalBasicTestAltinnFaultFaultFaultMessage {
		port.test();
	}

	@Test
	public void shouldSendAMessageToAltinnTest() throws MalformedURLException, CorrespondenceBuilderException, SerialException,
			SQLException {
		MessageDTO message = new MessageDTO();
		message.setMessageReference("REF001");
		message.setParticipantId("910013874");
		message.setCaseOfficer("Test Case Officer");
		message.setCaseDescription("Test Case Description");
		message.setMessageHeader("VM&nbsp;200900061,Main test altut,Deres ref&nbsp;E29723/soi/test");
		message.setMessageSummary("Må besvares innen:2009.06.01<br />Saksnummer:VM&nbsp;200900061&nbsp;<br />Brevtype:Main test altut<br />Deres ref:E29723/soi/test<br />Status:01000&nbsp;Under behandling&nbsp;Mottatt<br />Tittel:test sans player<br />Saksbehandler:soi");
		List<AttachmentDTO> attachments = new ArrayList<>();
		String string = "originalBase64encodedStreng";
		char[] content = string.toCharArray();
		Clob attachment = new SerialClob(content);
		String mimeType = "application/txt";
		String fileName = "filnavn";
		String name = "visningsnavn";
		String attachmentAsString = string;
		AttachmentDTO attachement = new AttachmentDTO(attachment, mimeType, fileName, name, attachmentAsString);
		attachments.add(attachement);
		message.setAttachments(attachments);

		try {
			ReceiptExternal receiptExternal = altinnWS.sendMessage(message);
			assertThat(receiptExternal.getReceiptStatusCode(), is(ReceiptStatusEnum.OK));
		} catch (ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage e) {
			AltinnFault faultInfo = e.getFaultInfo();
			fail("This tests will fail with incorrect keyValues.\n Message from Altinn: "
					+ faultInfo.getAltinnLocalizedErrorMessage().getValue());
		}
	}

}
