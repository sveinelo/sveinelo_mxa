/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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
import no.mxa.service.KeyValues;
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
	@Mock
	private KeyValues keyValues;
	private SendSingleCorrespondenceMessage singleCorrespondenceMessage;

	@Before
	public void setup() {
		singleCorrespondenceMessage = new SendSingleCorrespondenceMessage(altinnWS, messageService, logGenerator, logService,
				keyValues);
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
