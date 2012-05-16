package no.mxa.service.altut;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.inject.Inject;

import no.mxa.test.support.SpringBasedTest;

import org.hibernate.SessionFactory;
import org.junit.Test;

public class SendAltutMessageTest extends SpringBasedTest {
	@Inject
	SessionFactory sessionFactory;
	@Inject
	MessageSender sendAltutMessage;

	@Test
	public void shouldSendAltutMessageWithResultOk() {
		String testResult = "";
		String result = sendAltutMessage.sendMessages();

		if (result.indexOf("OK") > -1) {
			testResult = "OK";
		}
		assertThat(testResult, is("OK"));
	}

}
