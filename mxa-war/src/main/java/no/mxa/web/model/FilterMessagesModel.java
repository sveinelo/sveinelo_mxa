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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterMessagesModel {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterMessagesModel.class);
	private static final String EMPTY_STRING = "";
	private MessageService messageService;
	private MessageDTO criteria;
	private List<MessageDTO> results;
	private Date fromDate, toDate;
	private String messageReferenceLike; // Can be partial
	private String caseDescriptionLike; // Can be partial

	@Inject
	public FilterMessagesModel(MessageService messageService) {
		this.messageService = messageService;
		criteria = new MessageDTO();
	}

	public String search() {
		LOGGER.debug("Search started.");
		// Remember if new String fields are added in the GUI, then a .equals("") check must be added and set it to null.
		if (EMPTY_STRING.equals(messageReferenceLike)) {
			criteria.setMessageReference(null);
		}
		if (EMPTY_STRING.equals(criteria.getDomain())) {
			criteria.setDomain(null);
		}
		if (EMPTY_STRING.equals(criteria.getParticipantId())) {
			criteria.setParticipantId(null);
		}
		if (EMPTY_STRING.equals(caseDescriptionLike)) {
			criteria.setCaseDescription(null);
		}
		if (EMPTY_STRING.equals(criteria.getAltinnArchive())) {
			criteria.setAltinnArchive(null);
		}
		if (-1 == criteria.getMessageStatus()) {
			criteria.setMessageStatus(null);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Search criteria after \"\" removal: " + criteria.toString() + "end.");
		}

		results = messageService.searchFromGUI(criteria, fromDate, toDate, caseDescriptionLike, messageReferenceLike);
		LOGGER.debug("Search end.");
		return "success";
	}

	public String reset() {
		criteria = null;
		results = null;
		fromDate = null;
		toDate = null;
		messageReferenceLike = null;
		caseDescriptionLike = null;
		return "success";
	}

	public List<MessageDTO> getResults() {
		return results;
	}

	public void setResults(List<MessageDTO> results) {
		this.results = results;
	}

	public MessageDTO getCriteria() {
		return criteria;
	}

	public void setCriteria(MessageDTO criteria) {
		this.criteria = criteria;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getMessageReference() {
		return messageReferenceLike;
	}

	public void setMessageReference(String messageReference) {
		this.messageReferenceLike = messageReference;
	}

	public String getCaseDescription() {
		return caseDescriptionLike;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescriptionLike = caseDescription;
	}
}
