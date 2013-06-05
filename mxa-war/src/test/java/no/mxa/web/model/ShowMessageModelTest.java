/*
 * #%L
 * Web Archive
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
package no.mxa.web.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import no.mxa.dto.MessageDTO;
import no.mxa.service.KeyValues;
import no.mxa.service.MessageService;
import no.mxa.service.SendNoticeOrWarningService;
import no.mxa.web.FacesContextProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShowMessageModelTest {

	@Mock
	private KeyValues keyValues;

	@Mock
	private MessageService messageService;

	@Mock
	private SendNoticeOrWarningService sendNoticeOrWarningService;
	@Mock
	private FacesContextProvider facesContextProvider;
	private ShowMessageModel showMessageModel;

	@Before
	public void createShowMessageModel() throws Exception {
		showMessageModel = new ShowMessageModel(messageService, sendNoticeOrWarningService, keyValues, facesContextProvider);
	}

	@Test
	public void testReSendToAltinn() {
		String outcome = showMessageModel.reSendToAltinn();
		assertThat(outcome, is("success"));
	}

	@Test
	public void testChangeStatusToManuallyRemoved() {
		when(messageService.searchById(null)).thenReturn(new MessageDTO());

		String outcome = showMessageModel.changeStatusToManuallyRemoved();

		assertThat(outcome, is("success"));
	}

	@Test
	public void testSendWarn() {
		when(messageService.searchById(null)).thenReturn(new MessageDTO());
		
		String outcome = showMessageModel.sendWarn();
		
		assertThat(outcome, is("success"));
	}

}
