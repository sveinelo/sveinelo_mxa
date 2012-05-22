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
package no.mxa.service;

import java.util.Date;
import java.util.List;

import no.mxa.dto.MessageDTO;

public interface MessageService {

	/**
	 * 
	 * @param id
	 *            The id of the MessageDTO to retrieve
	 * @return The matching message DTO
	 */
	MessageDTO searchById(Long id);

	/**
	 * 
	 * @param propertyName
	 *            The name of the property, e.g messageId
	 * @param value
	 *            The value of the property
	 * @return A list of messageDTOs
	 */
	List<MessageDTO> searchByProperty(String propertyName, Object value);

	/**
	 * 
	 * @param log
	 *            The messageDTO to save via the repository
	 */
	void saveMessage(MessageDTO message);

	/**
	 * 
	 * @param message
	 *            The messageDTO to merge via the repository
	 */
	void mergeMessage(MessageDTO message);

	/**
	 * 
	 * @param criteria
	 *            MessageDTO object with N properties set.
	 * @return All messages matching the properties.
	 */
	List<MessageDTO> searchByCriteria(MessageDTO criteria);

	/**
	 * @param presentDate
	 *            The present data to be compared with the read deadline
	 * @return A list of messages with one or more deviations, e.g. failed in Altinn delivery
	 */
	List<MessageDTO> searchByMessageDeviations(Date presentDate);

	/**
	 * Used to find all messages where and overdue notice should be sent.
	 * 
	 * @return Messages which an overdue notice must be sent to contacts and Agency.
	 */
	List<MessageDTO> searchNoticeMessages();

	/**
	 * Used to find all messages where and warning should be sent. A "warning" is an e-mail with all attachments sent to a
	 * preset e-mail address from keyvalues. (MAILTOPAT)
	 * 
	 * @return Messages which an overdue notice must be sent to contacts and Agency.
	 */
	List<MessageDTO> searchWarnMessages();

	/**
	 * This method is used by the GUI to check if a message has any deviation.
	 * 
	 * @param messageKey
	 *            A messageKey to check for deviations
	 * @return TRUE if the input message has deviations, FALSE otherwise
	 */
	boolean hasDeviation(String messageKey);

	/**
	 * 
	 * @param messageKey
	 * @return One message matching the messageKey
	 */
	MessageDTO searchByMessageKey(String messageKey);

	/**
	 * 
	 * @param messageId
	 *            The id of the message to update
	 */
	void updateMessageStatusAndSentAltinnFlag(long messageId);

	/**
	 * 
	 * @param messageId
	 *            The id of the message to update
	 */
	void updateMessageStatus(long messageId);

	/**
	 * 
	 * @param criteria
	 *            The criteria to use in the search
	 * @param fromDate
	 *            The earliest date to be included in search result. Ignored if null
	 * @param toDate
	 *            The latest date to be included in search result. Ignored if null
	 * @param caseDescription
	 *            Possible additional criteria, ignored if null
	 * @param messageReference
	 *            Possible additional criteria, ignored if null
	 * @return All messages matching the expression
	 */
	List<MessageDTO> searchFromGUI(MessageDTO criteria, Date fromDate, Date toDate, String caseDescription,
			String messageReference);
}
