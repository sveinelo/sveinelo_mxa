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

import java.sql.Date;
import java.util.List;

/**
 * Class for keeping details of messages received from Agency
 */
public class Message {

	private String domain;
	private String sendingSystem;
	private int batchSending;
	private String caseDescription;
	private String caseOfficer;
	private String idproc;
	private String messageReference;
	private String participantId;
	private Date dueDate;
	private String altinnArchive;
	private String contentMessageHeader;
	private String contentMessageSummary;

	private List<Attachment> attachments;
	private List<ContactInfo> contactInfo;

	public Message() {

	}

	public Message(String domain, String sendingSystem, int batchSending, String caseDescription, String caseOfficer) {
		this.domain = domain;
		this.sendingSystem = sendingSystem;
		this.batchSending = batchSending;
		this.caseDescription = caseDescription;
		this.caseOfficer = caseOfficer;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSendingSystem() {
		return sendingSystem;
	}

	public void setSendingSystem(String sendingSystem) {
		this.sendingSystem = sendingSystem;
	}

	public int getBatchSending() {
		return batchSending;
	}

	public void setBatchSending(String batchSending) {
		if (batchSending.equalsIgnoreCase("true")) {
			this.batchSending = 1;
		} else {
			this.batchSending = 0;
		}
	}

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public String getCaseOfficer() {
		return caseOfficer;
	}

	public void setCaseOfficer(String caseOfficer) {
		this.caseOfficer = caseOfficer;
	}

	public String getIdproc() {
		return idproc;
	}

	public void setIdproc(String idproc) {
		this.idproc = idproc;
	}

	public String getMessageReference() {
		return messageReference;
	}

	public void setMessageReference(String messageReference) {
		this.messageReference = messageReference;
	}

	public String getMessageKey() {
		return messageReference + idproc;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = Date.valueOf(dueDate);
	}

	public String getAltinnArchive() {
		return altinnArchive;
	}

	public void setAltinnArchive(String altinnArchive) {
		this.altinnArchive = altinnArchive;
	}

	public String getContentMessageHeader() {
		return contentMessageHeader;
	}

	public void setContentMessageHeader(String contentMessageHeader) {
		this.contentMessageHeader = contentMessageHeader;
	}

	public String getContentMessageSummary() {
		return contentMessageSummary;
	}

	public void setContentMessageSummary(String contentMessageSummary) {
		this.contentMessageSummary = contentMessageSummary;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<ContactInfo> getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(List<ContactInfo> contactInfo) {
		this.contactInfo = contactInfo;
	}
}
