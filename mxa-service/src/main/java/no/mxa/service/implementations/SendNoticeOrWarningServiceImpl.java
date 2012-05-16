package no.mxa.service.implementations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import no.mxa.UniversalConstants;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.KeyValues;
import no.mxa.service.LogGenerator;
import no.mxa.service.MessageService;
import no.mxa.service.RecipientDTO;
import no.mxa.service.SendMailService;
import no.mxa.service.SendNoticeOrWarningService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendNoticeOrWarningServiceImpl implements SendNoticeOrWarningService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticeOrWarningServiceImpl.class);

	private LogGenerator logGenerator;
	private MessageService messageService;
	private KeyValues keyValues;
	private SendMailService sendMailService;

	@Inject
	public SendNoticeOrWarningServiceImpl(LogGenerator logGenerator, MessageService messageService, KeyValues keyValues,
			SendMailService sendMailService) {
		this.logGenerator = logGenerator;
		this.messageService = messageService;
		this.keyValues = keyValues;
		this.sendMailService = sendMailService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.SendNoticeOrWarningService#sendNoticeMail(no.mxa.dto.MessageDTO)
	 */
	@Override
	public void sendNoticeMail(MessageDTO message) {
		String subject = keyValues.getMailNoticeSubject();
		String from = keyValues.getMailFrom();
		ArrayList<RecipientDTO> recipients = new ArrayList<RecipientDTO>(); // List of all recipients
		try {
			for (ContactInfoDTO contactInfoDTO : message.getContactInfo()) {
				if (contactInfoDTO.getType().equals(UniversalConstants.CONTACTINFOTYPE_EMAIL)) {
					// Populate only with E-mail (not phone-numbers(SMS))
					recipients.add(new RecipientDTO(RecipientType.TO, new InternetAddress(contactInfoDTO.getAddress())));
				}
			}
			String messageText = keyValues.getMailNoticeContent();
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_MESSAGEHEADER, message.getMessageHeader());
			messageText = messageText.replaceAll("&nbsp;", " ");
			messageText = messageText.replaceAll("<br />", "\n");
			messageText = messageText.replaceAll("&lt;", "<");
			messageText = messageText.replaceAll("&gt;", ">");
			messageText = messageText.replaceAll("&quot;", "\"");
			messageText = messageText.replaceAll("&#39;", "'");
			messageText = messageText.replaceAll("&amp;", "&");

			sendMailService.sendMailMessage(recipients, subject, messageText, null, from);
			// Mail sent successful, write log entry and update status.
			message.getLog().add(
					logGenerator.generateLog("Sendt til: " + recipients.toString(),
							UniversalConstants.LOG_MAIL_OVERDUENOTICE_SENT, message.getId()));
			message.setOverdueNoticeSent(UniversalConstants.MSG_OVERDUENOTICE_TRUE);
			message.setReadDeadline(getFutureDate(new Date(), 7));
			messageService.mergeMessage(message);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send e-mail. MessageReference: " + message.getMessageReference(), e);
			logGenerator.generateLog("Sending av e-post feilet til.", UniversalConstants.MAIL_FAILED_OVERDUENOTICE,
					message.getId());
		} catch (SQLException e) {
			LOGGER.error("Failed to get Attachments from db.", e);
			logGenerator.generateLog("Sending av e-post feilet pga. vedlegg.", UniversalConstants.MAIL_FAILED_OVERDUEWARN,
					message.getId());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.SendNoticeOrWarningService#sendWarnMail(no.mxa.dto.MessageDTO)
	 */
	@Override
	public void sendWarnMail(MessageDTO message) {
		// Get From and Subject fields from keyValues
		String subject = keyValues.getMailWarnSubject();
		String from = keyValues.getMailFrom();
		// For each Message requiring notification, send mail.
		ArrayList<RecipientDTO> recipients = new ArrayList<RecipientDTO>(); // List of all recipients
		try {
			// Send mail only to Agency
			recipients.add(new RecipientDTO(RecipientType.TO, new InternetAddress(keyValues.getMailToPat())));
			String messageText = keyValues.getMailWarnContent();
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_MESSAGEHEADER, message.getMessageHeader());
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_MESSAGESUMMARY, message.getMessageSummary());
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_CASEDESCRIPTION, message.getCaseDescription());

			messageText = messageText.replaceAll("&nbsp;", " ");
			messageText = messageText.replaceAll("<br />", "\n");
			messageText = messageText.replaceAll("&lt;", "<");
			messageText = messageText.replaceAll("&gt;", ">");
			messageText = messageText.replaceAll("&quot;", "\"");
			messageText = messageText.replaceAll("&#39;", "'");
			messageText = messageText.replaceAll("&amp;", "&");

			sendMailService.sendMailMessage(recipients, subject, messageText, message.getAttachments(), from);
			// Mail sent successful, write log entry and update status.
			message.getLog().add(
					logGenerator.generateLog("Sendt e-post til: " + recipients.toString(),
							UniversalConstants.LOG_MAIL_OVERDUEWARN_SENT_TO_AGENCY, message.getId()));
			message.setMessageStatus(UniversalConstants.MSG_STATUS_OVERDUEWARN_SENT_TO_AGENCY);
			messageService.mergeMessage(message);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send e-mail. Messagereference: " + message.getMessageReference(), e);
			message.getLog().add(
					logGenerator.generateLog("Sending av e-post feilet til: " + recipients.toString(),
							UniversalConstants.MAIL_FAILED_OVERDUEWARN, message.getId()));
			messageService.mergeMessage(message);
		} catch (SQLException e) {
			LOGGER.error("Failed to get Attachments from db.", e);
			message.getLog().add(
					logGenerator.generateLog("Sending av e-post feilet pga. vedlegg til: " + recipients.toString(),
							UniversalConstants.MAIL_FAILED_OVERDUEWARN, message.getId()));
			messageService.mergeMessage(message);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.SendNoticeOrWarningService#sendGUIWarnMail(no.mxa.dto.MessageDTO)
	 */
	@Override
	public void sendGUIWarnMail(MessageDTO message) {
		// Get From and Subject fields from keyValues
		String subject = keyValues.getMailWarnSubject();
		String from = keyValues.getMailFrom();
		// For each Message requiring notification, send mail.
		ArrayList<RecipientDTO> recipients = new ArrayList<RecipientDTO>(); // List of all recipients
		try {
			// Send mail only to Agency
			recipients.add(new RecipientDTO(RecipientType.TO, new InternetAddress(keyValues.getMailToPat())));
			String messageText = keyValues.getMailWarnContent();
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_MESSAGEHEADER, message.getMessageHeader());
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_MESSAGESUMMARY, message.getMessageSummary());
			messageText = messageText.replaceAll(UniversalConstants.MAIL_PLCHLDR_CASEDESCRIPTION, message.getCaseDescription());

			messageText = messageText.replaceAll("&nbsp;", " ");
			messageText = messageText.replaceAll("<br />", "\n");
			messageText = messageText.replaceAll("&lt;", "<");
			messageText = messageText.replaceAll("&gt;", ">");
			messageText = messageText.replaceAll("&quot;", "\"");
			messageText = messageText.replaceAll("&#39;", "'");
			messageText = messageText.replaceAll("&amp;", "&");

			sendMailService.sendMailMessage(recipients, subject, messageText, message.getAttachments(), from);
			// Mail sent successful, write log entry and update status.
			message.getLog().add(
					logGenerator.generateLog("Sendt e-post til: " + recipients.toString(),
							UniversalConstants.LOG_MAIL_OVERDUEWARN_SENT_TO_AGENCY_FROM_GUI, message.getId()));
			messageService.mergeMessage(message);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send e-mail. Messagereference: " + message.getMessageReference(), e);
			message.getLog().add(
					logGenerator.generateLog("Sending av e-post feilet til: " + recipients.toString(),
							UniversalConstants.MAIL_FAILED_OVERDUEWARN, message.getId()));
			messageService.mergeMessage(message);
		} catch (SQLException e) {
			LOGGER.error("Failed to get Attachments from db.", e);
			message.getLog().add(
					logGenerator.generateLog("Sending av e-post feilet pga. vedlegg til: " + recipients.toString(),
							UniversalConstants.MAIL_FAILED_OVERDUEWARN, message.getId()));
			messageService.mergeMessage(message);
		}
	}

	/**
	 * @param readDeadline
	 * @param daysToAdd
	 * @return readDeadline+daysToAdd
	 */
	private Date getFutureDate(Date readDeadline, int daysToAdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(readDeadline);
		calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
		return calendar.getTime();
	}
}
