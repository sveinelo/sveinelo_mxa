package no.mxa.altinn.ws.api.itest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;

import javax.inject.Inject;

import no.mxa.altinn.ws.AltinnFault;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasic;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicTestAltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.altinn.ws.ReceiptStatusEnum;
import no.mxa.altinn.ws.api.AltinnWS;
import no.mxa.altinn.ws.api.CorrespondenceBuilderException;
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
	public void shouldSendAMessageToAltinnTest() throws MalformedURLException, CorrespondenceBuilderException {
		MessageDTO message = new MessageDTO();
		message.setMessageReference("REF001");
		message.setParticipantId("910013874");

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
