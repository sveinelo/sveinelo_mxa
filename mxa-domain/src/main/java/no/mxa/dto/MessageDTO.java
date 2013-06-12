/*
 * #%L
 * Domain
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
package no.mxa.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * DTO class which handles database mappings related to messages to be stored in the repository
 */
public class MessageDTO implements Serializable {
	/**
	 * Unique generated version number
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Private class variables
	 */
	private Long id;
	/** Used for authentication (and authorization) */
	private String messageKey;
	/** Identifies the sending system */
	private String sendingSystem;
	/** Flag to decide if the message will be delivered to Altinn as batch or via online web service call */
	private Integer batchSending;
	/** A two to ten letter code showing what type of case the documents relates to */
	private String domain;
	/** Mark denomination, product type, title etc. */
	private String caseDescription;
	/** Officer in charge, case officer or */
	private String caseOfficer;
	/** Social security number OR organization number */
	private String participantId;
	/** Unique message reference within a short name */
	private String messageReference;
	/** Key to authenticate the user when status link is opened */
	private String idproc;
	/** Deadline to confirm a message */
	private Date dueDate;
	/** Archive reference from Altinn */
	private String altinnArchive;
	/** Message headline */
	private String messageHeader;
	/** Message summary */
	private String messageSummary;
	/** Message sent to Altinn */
	private Integer sentAltinn;
	/** Timestamp when message was sent to Altinn. */
	private Timestamp sentAltinnDate;
	/** As defined in UniversalConstants.java */
	private Integer messageStatus;
	/** Deadline to read message in Altinn */
	private Date readDeadline;
	/** True if notice has been sent by email if message is not read in Altinn */
	private Integer overdueNoticeSent;
	private List<AttachmentDTO> attachments;
	private List<ContactInfoDTO> contactInfo;
	private List<LogDTO> log;

	/**
	 * Default constructor
	 */
	public MessageDTO() {
	}

	/**
	 * Constructor which takes message content as parameters
	 * 
	 * @param messageKey
	 *            Unique message key used as identificator
	 * @param sendingSystem
	 *            Where the message is sent from
	 * @param batchSending
	 *            If the message is to be delivered by batch or web service
	 * @param domain
	 *            Domain of the message
	 * @param caseDescription
	 *            Description of the related case
	 * @param caseOfficer
	 *            Officer in charge of the related case
	 * @param participantId
	 *            Social security number or organization number
	 * @param messageReference
	 *            First part of message key
	 * @param idproc
	 *            Second part of message key
	 * @param dueDate
	 *            Date to confirm message in Altinn
	 * @param altinnArchive
	 *            Archive reference from Altinn
	 * @param messageHeader
	 *            Title of the message
	 * @param messageSummary
	 *            Description of the message
	 * @param sentAltinn
	 *            If the message is sent to Altinn
	 * @param messageStatus
	 *            The status of the given message
	 * @param attachments
	 *            The list of attachments in the message
	 * @param contactInfo
	 *            The list of contactInfo in the message
	 * @param log
	 *            The list of log entries related to the message
	 */
	public MessageDTO(String messageKey, String sendingSystem, Integer batchSending, String domain, String caseDescription,
			String caseOfficer, String participantId, String messageReference, String idproc, Date dueDate,
			String altinnArchive, String messageHeader, String messageSummary, Integer sentAltinn, Integer messageStatus,
			List<AttachmentDTO> attachments, List<ContactInfoDTO> contactInfo, List<LogDTO> log) {
		this.messageKey = messageKey;
		this.sendingSystem = sendingSystem;
		this.batchSending = batchSending;
		this.domain = domain;
		this.caseDescription = caseDescription;
		this.caseOfficer = caseOfficer;
		this.participantId = participantId;
		this.messageReference = messageReference;
		this.idproc = idproc;
		this.dueDate = dueDate;
		this.altinnArchive = altinnArchive;
		this.messageHeader = messageHeader;
		this.messageSummary = messageSummary;
		this.sentAltinn = sentAltinn;
		this.messageStatus = messageStatus;
		this.attachments = attachments;
		this.contactInfo = contactInfo;
		this.log = log;
	}

	/**
	 * Getter method for id
	 * 
	 * @return: id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for id
	 * 
	 * @param id
	 *            : The id value to set on the message
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for messageKey
	 * 
	 * @return: messageKey
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * Encode the the key for use in URL (the #-char needs to be encoded)
	 * 
	 * @return
	 */
	public String getUrlEncodedMessageKey() {
		try {
			return URLEncoder.encode(getMessageKey(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Setter method for messageKey
	 * 
	 * @param messageKey
	 *            : The messageKey value in the incoming message
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * Getter method for sendingSystem
	 * 
	 * @return: sendingSystem
	 */
	public String getSendingSystem() {
		return sendingSystem;
	}

	/**
	 * Setter method for sendingSystem
	 * 
	 * @param sendingSystem
	 *            : The sendingSystem value in the incoming message
	 */
	public void setSendingSystem(String sendingSystem) {
		this.sendingSystem = sendingSystem;
	}

	/**
	 * Getter method for batchSending
	 * 
	 * @return: batchSending
	 */
	public Integer getBatchSending() {
		return batchSending;
	}

	/**
	 * Setter method for batchSending
	 * 
	 * @param batchSending
	 *            : The batchSending value in the incoming message
	 */
	public void setBatchSending(Integer batchSending) {
		this.batchSending = batchSending;
	}

	/**
	 * Getter method for domain
	 * 
	 * @return: domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Setter method for domain
	 * 
	 * @param domain
	 *            : The domain value in the incoming message
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Getter method for caseDescription
	 * 
	 * @return: caseDescription
	 */
	public String getCaseDescription() {
		return caseDescription;
	}

	/**
	 * Setter method for caseDescription
	 * 
	 * @param caseDescription
	 *            : The caseDescription value in the incoming message
	 */
	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	/**
	 * Getter method for caseOfficer
	 * 
	 * @return: caseOfficer
	 */
	public String getCaseOfficer() {
		return caseOfficer;
	}

	/**
	 * Setter method for caseOfficer
	 * 
	 * @param caseOfficer
	 *            : The caseOfficer value in the incoming message
	 */
	public void setCaseOfficer(String caseOfficer) {
		this.caseOfficer = caseOfficer;
	}

	/**
	 * Getter method for participantId
	 * 
	 * @return: participantId
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * Setter method for participantId
	 * 
	 * @param participantId
	 *            : The participantId value in the incoming message
	 */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * Getter method for messageReference
	 * 
	 * @return: messageReference
	 */
	public String getMessageReference() {
		return messageReference;
	}

	/**
	 * Setter method for messageReference
	 * 
	 * @param messageReference
	 *            : The messageReference value in the incoming message
	 */
	public void setMessageReference(String messageReference) {
		this.messageReference = messageReference;
	}

	/**
	 * Getter method for idproc
	 * 
	 * @return: idproc
	 */
	public String getIdproc() {
		return idproc;
	}

	/**
	 * Setter method for idproc
	 * 
	 * @param idproc
	 *            : The idproc value in the incoming message
	 */
	public void setIdproc(String idproc) {
		this.idproc = idproc;
	}

	/**
	 * Getter method for dueDate
	 * 
	 * @return: dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Setter method for dueDate
	 * 
	 * @param dueDate
	 *            : The dueDate value in the incoming message
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Getter method for altinnArchive
	 * 
	 * @return: altinnArchive
	 */
	public String getAltinnArchive() {
		return altinnArchive;
	}

	/**
	 * Setter method for altinnArchive
	 * 
	 * @param altinnArchive
	 *            : The altinnArchive value in the incoming message
	 */
	public void setAltinnArchive(String altinnArchive) {
		this.altinnArchive = altinnArchive;
	}

	/**
	 * Getter method for messageHeader
	 * 
	 * @return: messageHeader
	 */
	public String getMessageHeader() {
		return messageHeader;
	}

	/**
	 * Setter method for messageHeader
	 * 
	 * @param messageHeader
	 *            : The messageHeader value in the incoming message
	 */
	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * Getter method for messageSummary
	 * 
	 * @return: messageSummary
	 */
	public String getMessageSummary() {
		return messageSummary;
	}

	/**
	 * Setter method for messageSummary
	 * 
	 * @param messageSummary
	 *            : The messageSummary value in the incoming message
	 */
	public void setMessageSummary(String messageSummary) {
		this.messageSummary = messageSummary;
	}

	/**
	 * Getter method for sentAltinn
	 * 
	 * @return: sentAltinn
	 */
	public Integer getSentAltinn() {
		return sentAltinn;
	}

	/**
	 * Setter method for sentAltinn
	 * 
	 * @param sentAltinn
	 *            : The sentAltinn value in the incoming message
	 */
	public void setSentAltinn(Integer sentAltinn) {
		this.sentAltinn = sentAltinn;
	}

	/**
	 * Getter method for message status
	 * 
	 * @return: messageStatus
	 */
	public Integer getMessageStatus() {
		return messageStatus;
	}

	/**
	 * Setter method for messageStatus
	 * 
	 * @param messageStatus
	 *            : The messageStatus of the message
	 */
	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}

	/**
	 * Getter method for attachments
	 * 
	 * @return: attachments
	 */
	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	/**
	 * Setter method for attachments
	 * 
	 * @param attachments
	 *            : The attachments of the message
	 */
	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	/**
	 * Getter method for contactInfo
	 * 
	 * @return: contactInfo
	 */
	public List<ContactInfoDTO> getContactInfo() {
		return contactInfo;
	}

	/**
	 * Setter method for contactInfo
	 * 
	 * @param contactInfo
	 *            : The contactInfo of the message
	 */
	public void setContactInfo(List<ContactInfoDTO> contactInfo) {
		this.contactInfo = contactInfo;
	}

	/**
	 * Getter method for log
	 * 
	 * @return: log
	 */
	public List<LogDTO> getLog() {
		return log;
	}

	/**
	 * Setter method for log
	 * 
	 * @param log
	 *            : The log related to the message
	 */
	public void setLog(List<LogDTO> log) {
		this.log = log;
	}

	/**
	 * @return the readDeadline
	 */
	public Date getReadDeadline() {
		return readDeadline;
	}

	/**
	 * @param readDeadline
	 *            the readDeadline to set
	 */
	public void setReadDeadline(Date readDeadline) {
		this.readDeadline = readDeadline;
	}

	/**
	 * @return the overdueNoticeSent
	 */
	public Integer getOverdueNoticeSent() {
		return overdueNoticeSent;
	}

	/**
	 * @param overdueNoticeSent
	 *            the overdueNoticeSent to set
	 */
	public void setOverdueNoticeSent(Integer overdueNoticeSent) {
		this.overdueNoticeSent = overdueNoticeSent;
	}

	/**
	 * @return the sentAltinnDate
	 */
	public Timestamp getSentAltinnDate() {
		return sentAltinnDate;
	}

	/**
	 * @param sentAltinnDate
	 *            the sentAltinnDate to set
	 */
	public void setSentAltinnDate(Timestamp sentAltinnDate) {
		this.sentAltinnDate = sentAltinnDate;
	}

	@Override
	public String toString() {
		String returnString = "altinnArchive: " + altinnArchive + "\n" + "caseDescription: " + caseDescription + "\n"
				+ "caseOfficer: " + caseOfficer + "\n" + "domain: " + domain + "\n" + "idproc: " + idproc + "\n"
				+ "messageHeader: " + messageHeader + "\n" + "messageKey: " + messageKey + "\n" + "messageReference: "
				+ messageReference + "\n" + "messageSummary: " + messageSummary + "\n" + "participantId: " + participantId
				+ "\n" + "sendingSystem: " + sendingSystem + "\n" + "batchSending: " + batchSending + "\n" + "dueDate: "
				+ dueDate + "\n" + "id: " + id + "\n" + "messageStatus: " + messageStatus + "\n" + "sentAltinn: " + sentAltinn
				+ "\n" + "sentAltinnDate: " + sentAltinnDate + "\n" + "readDeadline: " + readDeadline + "\n"
				+ "overdueNoticeSent" + overdueNoticeSent + "\n";
		returnString = returnString.replaceAll(".*null\n", "");
		return returnString;
	}
}
