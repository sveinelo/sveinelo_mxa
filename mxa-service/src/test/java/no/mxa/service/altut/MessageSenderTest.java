package no.mxa.service.altut;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageSenderTest {
	@Mock
	private SingleMessageSender sendSingleAltutMessage;
	@Mock
	private MessageService messageService;
	@Mock
	private LogGenerator logGenerator;
	@Mock
	private LogService logService;

	private MessageSender sendAltutMessage;

	@Before
	public void setup() {
		sendAltutMessage = new MessageSender(sendSingleAltutMessage, messageService, logGenerator, logService);
	}

	@Test
	public void shouldReturnOk() {
		String messages = sendAltutMessage.sendMessages();
		assertThat(messages, is("OK: Succeded: 0"));
	}

}
