/*
 * #%L
 * Service
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
