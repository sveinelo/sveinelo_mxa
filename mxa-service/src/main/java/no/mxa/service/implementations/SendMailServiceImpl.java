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
package no.mxa.service.implementations;

import java.io.UnsupportedEncodingException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to send email to external parties.
 */
public class SendMailServiceImpl implements SendMailService {

	private final KeyValues keyValues;
	private final AttachmentService attachmentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SendMailServiceImpl.class);

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
					try {
						bp.setContent(new String(attachmentData, "UTF-8"), attachment.getMimeType());
					} catch (UnsupportedEncodingException e) {
						LOGGER.error("Encoding problem", e);
					}
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

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(keyValues.getSmtpUser(), keyValues.getSmtpPassword());
		}
	}
}
