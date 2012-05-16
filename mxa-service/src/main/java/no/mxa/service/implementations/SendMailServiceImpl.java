package no.mxa.service.implementations;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import no.mxa.dto.AttachmentDTO;
import no.mxa.service.AttachmentService;
import no.mxa.service.KeyValues;
import no.mxa.service.RecipientDTO;
import no.mxa.service.SendMailService;

/**
 * Class used to send email to external parties.
 */
public class SendMailServiceImpl implements SendMailService {

	private KeyValues keyValues;
	private AttachmentService attachmentService;

	@Inject
	public SendMailServiceImpl(KeyValues keyValues, AttachmentService attachmentService) {
		this.keyValues = keyValues;
		this.attachmentService = attachmentService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.implementations.SendMail#sendMailMessage(java.lang.String[], java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void sendMailMessage(List<RecipientDTO> recipients, String subject, String messageText,
			List<AttachmentDTO> attachments, String from) throws MessagingException, SQLException {
		// Set SMTP server
		Session session;

		Properties properties = new Properties();
		properties.put("mail.smtp.host", keyValues.getSmtpHost());
		if (keyValues.getSmtpUser() != null) {
			properties.put("mail.smtp.auth", "true");
			Authenticator authenticator = new SMTPAuthenticator();

			// Create session
			session = Session.getDefaultInstance(properties, authenticator);
		} else {
			session = Session.getDefaultInstance(properties);
		}

		// To debug the mail session, uncomment the following line.
		// session.setDebug(true);
		// Create message
		Message message = new MimeMessage(session);

		// Set from and to address and cc
		message.setFrom(new InternetAddress(from));

		for (RecipientDTO recipient : recipients) {
			message.addRecipient(recipient.getType(), recipient.getAddresst());
		}

		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(messageText);
		multipart.addBodyPart(messageBodyPart);

		if (attachments != null) {
			for (AttachmentDTO attachment : attachments) {
				BodyPart bp = new MimeBodyPart();
				byte[] attachmentData = attachmentService.getAttachmentAsByteArray(attachment.getId());
				if (attachment.getMimeType().equals("text/plain")) {
					bp.setContent(new String(attachmentData), attachment.getMimeType());
				} else {
					bp.setContent(attachmentData, attachment.getMimeType());
				}
				bp.setFileName(attachment.getFileName());
				multipart.addBodyPart(bp);
			}
		}

		message.setContent(multipart);

		// // Set Subject and content and content type
		message.setSubject(subject);
		// message.setContent(messageText,"text/plain");

		Transport.send(message);
	}

	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP server requires it.
	 */
	class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(keyValues.getSmtpUser(), keyValues.getSmtpPassword());
		}
	}
}