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

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;

/**
 * Provides a list of all messages with deviations.
 */
public class DeviationMessagesModel {
	private MessageService messageService;
	private List<MessageDTO> deviationList;

	@Inject
	public DeviationMessagesModel(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostConstruct
	public void populateDeviationList() {
		deviationList = messageService.searchByMessageDeviations(new Date());
	}

	public List<MessageDTO> getDeviationList() {
		return deviationList;
	}

}
