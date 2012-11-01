/*
 * #%L
 * Web Archive
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
package no.mxa.web.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeviationMessagesModelTest {
	@Mock
	private MessageService messageService;
	private DeviationMessagesModel model;

	@Before
	public void setup() {
		model = new DeviationMessagesModel(messageService);
	}

	@Test
	public void shouldFindDeviations() {
		List<MessageDTO> stubbedList = Collections.emptyList();
		when(messageService.searchByMessageDeviations(any(Date.class))).thenReturn(stubbedList);
		model.populateDeviationList();

		List<MessageDTO> deviationList = model.getDeviationList();

		assertThat(deviationList, is(stubbedList));
	}

}
