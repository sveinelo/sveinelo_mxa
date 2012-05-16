package no.mxa.service.altut;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;

import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.altinn.ws.ReceiptStatusEnum;
import no.mxa.altinn.ws.api.AltinnWS;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SendSingleCorrespondenceMessageTest {
	@Mock
	private AltinnWS altinnWS;
	@Mock
	private MessageService messageService;
	@Mock
	private LogGenerator logGenerator;
	@Mock
	private LogService logService;
	private SendSingleCorrespondenceMessage singleCorrespondenceMessage;

	@Before
	public void setup() {
		singleCorrespondenceMessage = new SendSingleCorrespondenceMessage(altinnWS, messageService, logGenerator, logService);
	}

	@Test
	public void shouldSendMessage() throws Exception {
		MessageDTO message = new MessageDTO();
		long id = 1L;
		message.setId(id);
		ReceiptExternal receipt = mock(ReceiptExternal.class);

		when(altinnWS.sendMessage(message)).thenReturn(receipt);
		when(messageService.searchById(id)).thenReturn(new MessageDTO());
		when(receipt.getReceiptStatusCode()).thenReturn(ReceiptStatusEnum.OK);

		when(logGenerator.generateLog(anyString(), anyString(), anyLong(), any(Timestamp.class))).thenReturn(new LogDTO());

		assertThat(singleCorrespondenceMessage.sendMessage(message), is(true));
		verify(altinnWS).sendMessage(message);
	}
}
