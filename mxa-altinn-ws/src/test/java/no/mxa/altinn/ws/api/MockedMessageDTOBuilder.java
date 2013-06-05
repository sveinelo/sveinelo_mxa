/*
 * #%L
 * Altinn Webservice
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
package no.mxa.altinn.ws.api;

import java.util.ArrayList;
import java.util.List;

import no.mxa.dto.AttachmentDTO;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.MessageDTO;

/**
 * Provide {@link MessageDTO} test data.
 * 
 */
public class MockedMessageDTOBuilder {

	protected MessageDTO buildMessage() {
		MessageDTO message = new MessageDTO();
		message.setParticipantId(getParticipantId());
		message.setMessageHeader(getMessageHeader());
		message.setMessageSummary(getMessageSummary());
		message.setAltinnArchive(getAltinnArchive());

		/** Attachments */
		List<AttachmentDTO> attachmentList = new ArrayList<AttachmentDTO>();
		attachmentList.add(buildAttachment(getFileName1()));
		attachmentList.add(buildAttachment(getFileName2()));
		message.setAttachments(attachmentList);

		/** ContactInfo */
		List<ContactInfoDTO> contactInfoList = new ArrayList<ContactInfoDTO>();
		contactInfoList.add(buildContactInfo("SMS", getPhoneNumber()));
		contactInfoList.add(buildContactInfo("EMAIL", getEmailAddress()));
		message.setContactInfo(contactInfoList);

		return message;
	}

	private AttachmentDTO buildAttachment(String fileName) {
		AttachmentDTO attachment = new AttachmentDTO();
		attachment.setFileName(fileName);
		attachment.setBase64EncodedAttachement("TVhBLWRva3VtZW50IHRpbCBBbHR1dC4K");
		attachment.setName(getName());

		return attachment;
	}

	private ContactInfoDTO buildContactInfo(String type, String address) {
		ContactInfoDTO contactInfo = new ContactInfoDTO();
		contactInfo.setType(type);
		contactInfo.setAddress(address);

		return contactInfo;
	}

	protected String getGovOrgan() {
		return "SER";
	}

	protected String getParticipantId() {
		return "0101010101010";
	}

	protected String getLanguageCode() {
		return "1044";
	}

	protected String getMessageHeader() {
		return "Message header";
	}

	protected String getMessageSummary() {
		return "Message summary";
	}

	protected String getAltinnArchive() {
		return "AM1234";
	}

	protected String getFileName1() {
		return "File name 1";
	}

	protected String getFileName2() {
		return "File name 2";
	}

	protected String getName() {
		return "Name";
	}

	protected String getPhoneNumber() {
		return "12345678";
	}

	protected String getEmailAddress() {
		return "test@test.no";
	}

	protected String getSenderEmailAddress() {
		return "mxa@foo.bar";
	}

	protected String getNotificationType() {
		return "Correspondence";
	}

	protected String getServiceCode() {
		return "SER";
	}

	protected String getServiceEdition() {
		return "1";
	}

}
