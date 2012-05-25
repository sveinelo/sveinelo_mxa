/*
 * #%L
 * Altinn Webservice
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
package no.mxa.altinn.ws.api;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

import java.util.Calendar;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import no.mxa.altinn.ws.AttachmentsV2;
import no.mxa.altinn.ws.BinaryAttachmentExternalBEV2List;
import no.mxa.altinn.ws.BinaryAttachmentV2;
import no.mxa.altinn.ws.ExternalContentV2;
import no.mxa.altinn.ws.InsertCorrespondenceV2;
import no.mxa.altinn.ws.Notification;
import no.mxa.altinn.ws.NotificationBEList;
import no.mxa.altinn.ws.ObjectFactory;
import no.mxa.altinn.ws.ReceiverEndPoint;
import no.mxa.altinn.ws.ReceiverEndPointBEList;
import no.mxa.altinn.ws.TransportType;
import no.mxa.dto.AttachmentDTO;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.utils.DateUtils;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Build message on correspondence format.
 * 
 */
public class CorrespondenceBuilder {
	private final MessageValues messageValues;
	private final ObjectFactory objectFactory;

	/**
	 * Constructor.
	 * 
	 * @param messageValues
	 *            the {@link MessageValues} interface.
	 */
	public CorrespondenceBuilder(MessageValues messageValues) {
		this.messageValues = messageValues;
		objectFactory = new ObjectFactory();
	}

	/**
	 * Build {@link InsertCorrespondenceV2} from input {@link MessageDTO}.
	 * 
	 * @param message
	 *            the {@link MessageDTO} object to retrieve values from
	 * @return an {@link InsertCorrespondenceV2} object on the correspondence format
	 * @throws CorrespondenceBuilderException
	 */
	public InsertCorrespondenceV2 buildInsertCorrespondenceV2FromMessageDTO(MessageDTO message)
			throws CorrespondenceBuilderException {
		/** Correspondences.Correspondence */
		InsertCorrespondenceV2 insertCorrespondenceV2 = createCorrespondenceV2();
		/** Correspondences.Correspondence.Content */
		ExternalContentV2 externalContentV2 = createExternalContentV2();
		/** Correspondences.Correspondence.Content.Attachments */
		AttachmentsV2 attachmentsV2 = createAttachmentsV2();
		/** Correspondences.Correspondence.Content.Attachments.BinaryAttachments */
		BinaryAttachmentExternalBEV2List binaryAttachmentExternalBEV2List = createAttachmentExternalBEV2List();
		/**
		 * Correspondences.Correspondence.Content.Attachments.BinaryAttachments. BinaryAttachment
		 */
		BinaryAttachmentV2 binaryAttachmentV2 = null;
		/** Correspondences.Correspondence.Notifications */
		NotificationBEList notificationBEList = createNotificationBEList();
		/** Correspondences.Correspondence.Notifications.Notification */
		Notification notification = null;
		/**
		 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints
		 */
		ReceiverEndPointBEList receiverEndPointBEList = createReceiverEndPointBEList();
		/**
		 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints.ReceiverEndPoint
		 */
		ReceiverEndPoint receiverEndPoint = null;

		/** Correspondences.Correspondence */
		insertCorrespondenceV2.setServiceCode(getServiceCode());
		insertCorrespondenceV2.setServiceEdition(getServiceEdition());
		insertCorrespondenceV2.setReportee(getReportee(message));
		insertCorrespondenceV2.setAllowSystemDeleteDateTime(getAllowSystemDeleteDateTime());
		insertCorrespondenceV2.setDueDateTime(getDueDateTime());
		insertCorrespondenceV2.setArchiveReference(getArchiveReference(message));

		/** Correspondences.Correspondence.Content */
		externalContentV2.setLanguageCode(getLanguageCode());
		externalContentV2.setMessageTitle(getMessageTitle(message));
		externalContentV2.setMessageBody(getMessageBody(message));

		/**
		 * Correspondences.Correspondence.Content.Attachments.BinaryAttachments. BinaryAttachment
		 */
		if (message.getAttachments() != null && message.getAttachments().size() > 0) {
			for (AttachmentDTO attachmentDTO : message.getAttachments()) {
				binaryAttachmentV2 = createBinaryAttachmentV2();

				binaryAttachmentV2.setFileName(getBinaryAttachmentFileName(attachmentDTO));
				binaryAttachmentV2.setName(getBinaryAttachmentName(attachmentDTO));
				binaryAttachmentV2.setData(getBinaryAttachmentData(attachmentDTO));

				/** Add attachment in list */
				binaryAttachmentExternalBEV2List.getBinaryAttachmentV2().add(binaryAttachmentV2);
			}
		}

		/** Correspondences.Correspondence.Notifications.Notification */
		if (message.getContactInfo() != null && message.getContactInfo().size() > 0) {
			notification = createNotification();
			notification.setFromAddress(getFromAddress());
			notification.setLanguageCode(getLanguageCode());
			notification.setNotificationType(getNotificationType());

			for (ContactInfoDTO contactInfoDTO : message.getContactInfo()) {
				receiverEndPoint = createReceiverEndPoint();
				receiverEndPoint.setTransportType(getTransportType(contactInfoDTO));
				receiverEndPoint.setReceiverAddress(getReceiverAddress(contactInfoDTO));

				/** Add receiverEndpoint in list */
				receiverEndPointBEList.getReceiverEndPoint().add(receiverEndPoint);
			}

			/** Add receiverEndPoint in notification */
			notification.setReceiverEndPoints(getReceiverEndPointBEList(receiverEndPointBEList));
			/** Add notification in list */
			notificationBEList.getNotification().add(notification);
		}

		/** Correspondences.Correspondence.Content.Attachments.BinaryAttachments */
		attachmentsV2.setBinaryAttachments(getBinaryAttachmentExternalBEV2List(binaryAttachmentExternalBEV2List));

		/** Set attachments on content */
		externalContentV2.setAttachments(getAttachmentsV2(attachmentsV2));
		/** Set content on correspondence */
		insertCorrespondenceV2.setContent(getExternalContentV2(externalContentV2));
		/** Set notifications on correspondence */
		insertCorrespondenceV2.setNotifications(getNotificationBEList(notificationBEList));

		return insertCorrespondenceV2;
	}

	/** Correspondences.Correspondence.ServiceCode */
	private JAXBElement<String> getServiceCode() {
		return objectFactory.createInsertCorrespondenceV2ServiceCode(messageValues.getServiceCode());
	}

	/** Correspondences.Correspondence.ServiceEdition */
	private JAXBElement<String> getServiceEdition() {
		return objectFactory.createInsertCorrespondenceV2ServiceEdition(messageValues.getServiceEdition());
	}

	/** Correspondences.Correspondence.Reportee */
	private JAXBElement<String> getReportee(MessageDTO message) {
		return objectFactory.createInsertCorrespondenceV2Reportee(message.getParticipantId());
	}

	/**
	 * Correspondences.Correspondence.AllowSystemDeleteDate
	 * 
	 * @throws CorrespondenceBuilderException
	 * 
	 */
	private JAXBElement<XMLGregorianCalendar> getAllowSystemDeleteDateTime() throws CorrespondenceBuilderException {
		try {
			XMLGregorianCalendar allowSystemDeleteDateTime = DateUtils.getFutureDate(Calendar.YEAR, 5, 0);
			return objectFactory.createInsertCorrespondenceV2AllowSystemDeleteDateTime(allowSystemDeleteDateTime);
		} catch (DatatypeConfigurationException e) {
			throw new CorrespondenceBuilderException("Failed to create allowSystemDeleteDateTime", e);
		}

	}

	/**
	 * Correspondences.Correspondence.DueDateTime
	 * 
	 * @throws CorrespondenceBuilderException
	 */
	private XMLGregorianCalendar getDueDateTime() throws CorrespondenceBuilderException {
		try {
			return DateUtils.getFutureDate(Calendar.DAY_OF_MONTH, 7, 0);
		} catch (DatatypeConfigurationException e) {
			throw new CorrespondenceBuilderException("Failed to create dueDateTime", e);
		}
	}

	/** Correspondences.Correspondence.ArchiveReference */
	private JAXBElement<String> getArchiveReference(MessageDTO message) {
		return objectFactory.createInsertCorrespondenceV2ArchiveReference(message.getAltinnArchive());
	}

	/** Correspondences.Correspondence.Content */
	private JAXBElement<ExternalContentV2> getExternalContentV2(ExternalContentV2 externalContentV2) {
		return objectFactory.createInsertCorrespondenceV2Content(externalContentV2);
	}

	/** Correspondences.Correspondence.Content.LanguageCode */
	/** Correspondences.Correspondence.Notifications.Notification.LanguageCode */
	private JAXBElement<String> getLanguageCode() {
		return objectFactory.createExternalContentV2LanguageCode(messageValues.getLanguageCode());
	}

	/** Correspondences.Correspondence.Notifications.Notification.FromAddress */
	private JAXBElement<String> getFromAddress() {
		return objectFactory.createExternalContentV2LanguageCode(messageValues.getMailFrom());
	}

	/** Correspondences.Correspondence.Content.MessageTitle */
	private JAXBElement<String> getMessageTitle(MessageDTO message) {
		return objectFactory.createExternalContentV2MessageTitle(StringEscapeUtils.unescapeHtml(message.getMessageHeader()));
	}

	/** Correspondences.Correspondence.Content.MessageBody */
	private JAXBElement<String> getMessageBody(MessageDTO message) {
		return objectFactory.createExternalContentV2MessageBody(StringEscapeUtils.unescapeHtml(message.getMessageSummary()));
	}

	/** Correspondences.Correspondence.Content.Attachments */
	private JAXBElement<AttachmentsV2> getAttachmentsV2(AttachmentsV2 attachmentsV2) {
		return objectFactory.createExternalContentV2Attachments(attachmentsV2);
	}

	/** Correspondences.Correspondence.Content.Attachments.BinaryAttachments */
	private JAXBElement<BinaryAttachmentExternalBEV2List> getBinaryAttachmentExternalBEV2List(
			BinaryAttachmentExternalBEV2List binaryAttachmentExternalBEV2List) {
		return objectFactory.createAttachmentsV2BinaryAttachments(binaryAttachmentExternalBEV2List);
	}

	/**
	 * Correspondences.Correspondence.Content.Attachments.BinaryAttachments. BinaryAttachment.FileName
	 */
	private JAXBElement<String> getBinaryAttachmentFileName(AttachmentDTO attachment) {
		return objectFactory.createBinaryAttachmentV2FileName(attachment.getFileName());
	}

	/**
	 * Correspondences.Correspondence.Content.Attachments.BinaryAttachments. BinaryAttachment.Name
	 */
	private JAXBElement<String> getBinaryAttachmentName(AttachmentDTO attachment) {
		return objectFactory.createBinaryAttachmentV2Name(attachment.getName());
	}

	/**
	 * Correspondences.Correspondence.Content.Attachments.BinaryAttachments. BinaryAttachment.Data
	 */
	private JAXBElement<byte[]> getBinaryAttachmentData(AttachmentDTO attachment) {
		JAXBElement<byte[]> binaryAttachmentData = null;
		String base64EncodedAttachement = attachment.getBase64EncodedAttachement();
		byte[] decodedattachment = decodeBase64(base64EncodedAttachement);
		binaryAttachmentData = objectFactory.createBinaryAttachmentV2Data(decodedattachment);
		return binaryAttachmentData;
	}

	/** Correspondences.Correspondence.Notifications */
	private JAXBElement<NotificationBEList> getNotificationBEList(NotificationBEList notificationBEList) {
		return objectFactory.createInsertCorrespondenceNotifications(notificationBEList);
	}

	/**
	 * Correspondences.Correspondence.Notifications.Notification. NotificationType
	 */
	private JAXBElement<String> getNotificationType() {
		return objectFactory.createNotificationNotificationType(messageValues.getNotificationType());
	}

	/**
	 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints.ReceiverEndPoint.TransportType
	 */
	private JAXBElement<TransportType> getTransportType(ContactInfoDTO contactInfo) {
		TransportType transportType = null;
		if (contactInfo.getType().equals("EMAIL")) {
			transportType = TransportType.EMAIL;
		} else {
			transportType = TransportType.SMS;
		}

		return objectFactory.createReceiverEndPointTransportType(transportType);
	}

	/**
	 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints.ReceiverEndPoint.ReceiverAddress
	 */
	private JAXBElement<String> getReceiverAddress(ContactInfoDTO contactInfo) {
		return objectFactory.createReceiverEndPointReceiverAddress(contactInfo.getAddress());
	}

	/**
	 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints
	 */
	private JAXBElement<ReceiverEndPointBEList> getReceiverEndPointBEList(ReceiverEndPointBEList receiverEndPointBEList) {
		return objectFactory.createReceiverEndPointBEList(receiverEndPointBEList);
	}

	/** Correspondences.Correspondence */
	private InsertCorrespondenceV2 createCorrespondenceV2() {
		return objectFactory.createInsertCorrespondenceV2();
	}

	/** Correspondences.Correspondence.Content */
	private ExternalContentV2 createExternalContentV2() {
		return objectFactory.createExternalContentV2();
	}

	/** Correspondences.Correspondence.Content.Attachments */
	private AttachmentsV2 createAttachmentsV2() {
		return objectFactory.createAttachmentsV2();
	}

	/** Correspondences.Correspondence.Content.Attachments.BinaryAttachments */
	private BinaryAttachmentExternalBEV2List createAttachmentExternalBEV2List() {
		return objectFactory.createBinaryAttachmentExternalBEV2List();
	}

	/**
	 * Correspondences.Correspondence.Content.Attachments.BinaryAttachments. BinaryAttachment
	 */
	private BinaryAttachmentV2 createBinaryAttachmentV2() {
		return objectFactory.createBinaryAttachmentV2();
	}

	/** Correspondences.Correspondence.Notifications */
	private NotificationBEList createNotificationBEList() {
		return objectFactory.createNotificationBEList();
	}

	/** Correspondences.Correspondence.Notifications.Notification */
	private Notification createNotification() {
		return objectFactory.createNotification();
	}

	/**
	 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints
	 */
	private ReceiverEndPointBEList createReceiverEndPointBEList() {
		return objectFactory.createReceiverEndPointBEList();
	}

	/**
	 * Correspondences.Correspondence.Notifications.Notification. ReceiverEndPoints.ReceiverEndPoint
	 */
	private ReceiverEndPoint createReceiverEndPoint() {
		return objectFactory.createReceiverEndPoint();
	}

}
