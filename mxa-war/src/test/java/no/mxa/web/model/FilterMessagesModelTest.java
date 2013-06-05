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
import static org.mockito.Mockito.verify;
import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FilterMessagesModelTest {
	@Mock
	private MessageService messageService;
	private FilterMessagesModel filterMessageModel;

	@Before
	public void setUp() {
		filterMessageModel = new FilterMessagesModel(messageService);
	}

	@Test
	public void testFilterMessagesModelSearchByCriteria() {
		String messageReference = "VM-Partial";

		MessageDTO criteria = new MessageDTO();
		criteria.setMessageStatus(10);
		filterMessageModel.setCriteria(criteria);
		filterMessageModel.setMessageReference(messageReference);

		String outcome = filterMessageModel.search();

		assertThat(outcome, is("success"));
		verify(messageService).searchFromGUI(criteria, null, null, null, messageReference);
	}
}
