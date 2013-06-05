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

import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.service.KeyValues;
import no.mxa.service.MessageService;
import no.mxa.service.SendNoticeOrWarningService;
import no.mxa.web.FacesContextProvider;

/**
 * Provides all data necessary to display a message, and a couple of methods triggered by buttons in the front-end.
 */
public class ShowMessageModel {
	private MessageService messageService;
	private SendNoticeOrWarningService sendNoticeOrWarningService;
	private KeyValues keyValues;
	private FacesContextProvider facesContextProvider;

	private MessageDTO message;
	private boolean deviation; // Used to decide if deviation field should be shown in front-end.
	private String email; // MAILTOPAT
	private String buttonFeedback = "";

	@Inject
	public ShowMessageModel(MessageService messageService, SendNoticeOrWarningService sendNoticeOrWarningService,
			KeyValues keyValues, FacesContextProvider facesContextProvider) {
		this.messageService = messageService;
		this.sendNoticeOrWarningService = sendNoticeOrWarningService;
		this.keyValues = keyValues;
		this.facesContextProvider = facesContextProvider;
		init();
	}

	private void init() {
		email = keyValues.getMailToPat();
		String messageKey = facesContextProvider.getRequestParameter("messageKey");

		if (messageKey != null) {
			message = messageService.searchByMessageKey(messageKey);
			deviation = messageService.hasDeviation(messageKey);
		} else if (message != null) {
			// User has pushed a button
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
		if (message != null && message.getId() != null) {
			messageService.updateMessageStatusAndSentAltinnFlag(message.getId());
			message = messageService.searchById(message.getId());
			deviation = messageService.hasDeviation(message.getMessageKey());
			buttonFeedback = "Meldingen merket for resending til Altinn";
			return "success";
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
				messageService.updateMessageStatus(message.getId());
				message = messageService.searchById(message.getId());
				deviation = messageService.hasDeviation(message.getMessageKey());
				buttonFeedback = "Meldingen merket som \"Sak fjernet fra MXA manuelt\"";
			} else {
				message = messageService.searchById(message.getId());
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
			message = messageService.searchById(message.getId());
			sendNoticeOrWarningService.sendGUIWarnMail(message);
			deviation = messageService.hasDeviation(message.getMessageKey());
			buttonFeedback = "E-post har blitt sendt til " + keyValues.getMailToPat();
		}
		return "success";
	}

	public MessageDTO getMessage() {
		return message;
	}

	public boolean getDeviation() {
		return deviation;
	}

	public String getEmail() {
		return email;
	}

	public String getButtonFeedback() {
		return buttonFeedback;
	}

}
