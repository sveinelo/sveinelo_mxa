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
package no.mxa.ws.parser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.dto.AttachmentDTO;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.KeyValues;
import no.mxa.utils.DateUtils;

/**
 * Class that receives messages with attachments and contact info to be returned as DTO objects
 */
public class DTOGenerator {

	private final KeyValues keyValues;

	@Inject
	public DTOGenerator(KeyValues keyValues) {
		this.keyValues = keyValues;
	}

	/**
	 * 
	 * @param inputMessage
	 *            The message returned from the xml parser
	 * @return messageDTO The message DTO object, containing attachments and contact info
	 */
	public MessageDTO generateMessageDTO(Message inputMessage) {
		assert (inputMessage != null) : "Input should not be null";
		Attachment attachment;
		ContactInfo contactInfo;
		List<Attachment> inputAttachments;
		List<ContactInfo> inputContactInfo;
		MessageDTO messageDTO;
		AttachmentDTO attachmentDTO;
		ContactInfoDTO contactInfoDTO;
		List<AttachmentDTO> attachmentsToInclude;
		List<ContactInfoDTO> contactInfoToInclude;
		List<LogDTO> logInfoToInclude;
		LogDTO logDTO;

		// Create a new message DTO
		messageDTO = new MessageDTO();
		attachmentsToInclude = new ArrayList<AttachmentDTO>();
		contactInfoToInclude = new ArrayList<ContactInfoDTO>();
		logInfoToInclude = new ArrayList<LogDTO>();

		// Get the attached message list objects
		inputAttachments = inputMessage.getAttachments();
		inputContactInfo = inputMessage.getContactInfo();

		// Extracts the attachment objects
		if (inputAttachments != null) {
			for (Iterator<Attachment> itAtt = inputAttachments.iterator(); itAtt.hasNext();) {
				attachment = itAtt.next();
				attachmentDTO = new AttachmentDTO();

				// Set attachmentDTO values based on the incoming attachment values
				attachmentDTO.setBase64EncodedAttachement(attachment.getBase64EncodedString());
				attachmentDTO.setFileName(attachment.getFilename());
				attachmentDTO.setMimeType(attachment.getMimeType());
				attachmentDTO.setName(attachment.getName());

				// Set parent
				attachmentDTO.setMessage(messageDTO);

				// Adds the attachmentDTO object in the list to include in the
				// messageDTO object
				attachmentsToInclude.add(attachmentDTO);
			}
		}
		// Extracts the contact info objects
		if (inputContactInfo != null) {
			for (Iterator<ContactInfo> itCont = inputContactInfo.iterator(); itCont.hasNext();) {
				contactInfo = itCont.next();
				contactInfoDTO = new ContactInfoDTO();

				// Set the contactInfoDTO values based on the incoming
				// contactInfo values
				contactInfoDTO.setAddress(contactInfo.getAddress());
				contactInfoDTO.setType(contactInfo.getType());

				// Set parent
				contactInfoDTO.setMessage(messageDTO);

				// Adds the contactInfoDTO object in the list to include in the
				// contactInfoDTO object
				contactInfoToInclude.add(contactInfoDTO);
			}
		}

		// Set messageDTO values based on the incoming message values
		messageDTO.setAltinnArchive(inputMessage.getAltinnArchive());
		messageDTO.setBatchSending(inputMessage.getBatchSending());
		messageDTO.setCaseDescription(inputMessage.getCaseDescription());
		messageDTO.setCaseOfficer(inputMessage.getCaseOfficer());
		messageDTO.setDomain(inputMessage.getDomain());
		messageDTO.setDueDate(inputMessage.getDueDate());
		messageDTO.setIdproc(inputMessage.getIdproc());
		messageDTO.setMessageHeader(inputMessage.getContentMessageHeader());
		messageDTO.setMessageKey(inputMessage.getMessageKey());
		messageDTO.setMessageReference(inputMessage.getMessageReference());
		messageDTO.setMessageSummary(inputMessage.getContentMessageSummary());
		messageDTO.setParticipantId(inputMessage.getParticipantId());
		messageDTO.setSendingSystem(inputMessage.getSendingSystem());
		messageDTO.setSentAltinn(UniversalConstants.MSG_SENTALTINN_FALSE);
		messageDTO.setMessageStatus(UniversalConstants.MSG_STATUS_RECEIVED);
		messageDTO.setReadDeadline(DateUtils.getFutureDate(new Date(), keyValues.getMailNoticeDays()));
		messageDTO.setOverdueNoticeSent(UniversalConstants.MSG_OVERDUENOTICE_FALSE);
		messageDTO.setAttachments(attachmentsToInclude);
		messageDTO.setContactInfo(contactInfoToInclude);

		// Set log info
		logDTO = this.generateLogDTO();
		logDTO.setMessage(messageDTO);
		logInfoToInclude.add(logDTO);

		messageDTO.setLog(logInfoToInclude);

		// Returns a message DTO containing attachments, contact info and log
		return messageDTO;
	}

	/**
	 * 
	 * @return logDTO A log DTO object to be stored in the repository
	 */
	private LogDTO generateLogDTO() {
		LogDTO logDTO;
		// Create a new logDTO
		logDTO = new LogDTO();

		// Set logDTO values
		logDTO.setLogMessage("");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		logDTO.setTime(time);
		logDTO.setLogType(UniversalConstants.MSG_RECEIVED_MXA);
		logDTO.setLogMessage(UniversalConstants.MESSAGE_RECEIVED_MXA_DESCRIPTION);

		// Returns a log DTO
		return logDTO;
	}
}
