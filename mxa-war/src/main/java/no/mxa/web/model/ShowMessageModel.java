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

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.service.KeyValues;
import no.mxa.service.MessageService;
import no.mxa.service.SendNoticeOrWarningService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides all data necessary to display a message, and a couple of methods triggered by buttons in the front-end.
 */
public class ShowMessageModel {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowMessageModel.class);

	private MessageService messageService;
	private SendNoticeOrWarningService sendNoticeOrWarningService;
	private KeyValues keyValues;

	private MessageDTO message;
	private boolean deviation; // Used to decide if deviation field should be shown in front-end.
	private String email; // MAILTOPAT
	private String buttonFeedback = "";

	@Inject
	public ShowMessageModel(MessageService messageService, SendNoticeOrWarningService sendNoticeOrWarningService,
			KeyValues keyValues) {
		this.messageService = messageService;
		this.sendNoticeOrWarningService = sendNoticeOrWarningService;
		this.keyValues = keyValues;
	}

	@PostConstruct
	public void checkRequstParameterAndSetMessage() {
		email = keyValues.getMailToPat();
		FacesContext context = FacesContext.getCurrentInstance();
		String messageKey = context.getExternalContext().getRequestParameterMap().get("messageKey"); // For use by external
																										// pages
		if (messageKey != null) {
			// When a page links to this page it will send messageKey parameter.
			LOGGER.debug("messageKey " + messageKey);
			this.message = messageService.searchByMessageKey(messageKey);
			// Checks if message has deviation.
			this.deviation = messageService.hasDeviation(messageKey);
		} else if (message != null) {
			LOGGER.debug("User has pushed a button");
		} else {
			// User has accessed the page directly without request parameter.
			message = new MessageDTO(); // Creating empty MessageDTO to avoid NullpointerException. Resulting in a page with
										// null values
		}
	}

	/**
	 * Updates the message status to 10 (MSG_STATUS_RECEIVED), and sets sentAltinnFlagg=1. That way the message will be resent
	 * to Altinn shortly. (probably within 10 minutes, depending on the cronTrigger)
	 * 
	 * @return
	 */
	public String reSendToAltinn() {
		if (message != null) {
			messageService.updateMessageStatusAndSentAltinnFlag(message.getId());
			// Update message with the new values for presentation in GUI.
			this.message = messageService.searchById(message.getId());
			// Update hasDeviation field.
			this.deviation = messageService.hasDeviation(message.getMessageKey());
			buttonFeedback = "Meldingen merket for resending til Altinn";
		}
		return "success";
	}

	/**
	 * Updates the message status to 70 (MSG_STATUS_MANUALLY_REMOVED). That way the message will be removed from the deviation
	 * view.
	 * 
	 * @return
	 */
	public String changeStatusToManuallyRemoved() {
		if (message != null) {
			deviation = messageService.hasDeviation(message.getMessageKey());
			if (deviation) {
				// Update message status
				messageService.updateMessageStatus(message.getId());
				// Update message with the new values for presentation in GUI.
				message = messageService.searchById(message.getId());
				// Update hasDeviation field.
				deviation = messageService.hasDeviation(message.getMessageKey());
				buttonFeedback = "Meldingen merket som \"Sak fjernet fra MXA manuelt\"";
			} else {
				// Keep message.
				message = messageService.searchById(message.getId());
				// Update hasDeviation field.
				deviation = messageService.hasDeviation(message.getMessageKey());
				buttonFeedback = "Meldingen har ingen avvik som kan fjernes";
			}
		}
		return "success";
	}

	/**
	 * Sends an e-mail to MAILTOPAT with this message and all of its attachments.
	 * 
	 * @return
	 */
	public String sendWarn() {
		if (message != null) {
			this.message = messageService.searchById(message.getId());
			sendNoticeOrWarningService.sendGUIWarnMail(message);
			// Update hasDeviation field.
			this.deviation = messageService.hasDeviation(message.getMessageKey());
			buttonFeedback = "E-post har blitt sendt til " + keyValues.getMailToPat();

		}
		return "success";
	}

	public MessageDTO getMessage() {
		return message;
	}

	public void setMessage(MessageDTO message) {
		this.message = message;
	}

	public boolean getDeviation() {
		return deviation;
	}

	public void setDeviation(boolean deviation) {
		this.deviation = deviation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getButtonFeedback() {
		return buttonFeedback;
	}

	public void setButtonFeedback(String buttonFeedback) {
		this.buttonFeedback = buttonFeedback;
	}
}
