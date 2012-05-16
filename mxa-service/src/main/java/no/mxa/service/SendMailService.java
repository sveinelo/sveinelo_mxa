package no.mxa.service;

import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import no.mxa.dto.AttachmentDTO;

public interface SendMailService {

	/**
	 * 
	 * @param recipients
	 * @param subject
	 * @param messageText
	 * @param attachments
	 * @param from
	 * @throws MessagingException
	 * @throws SQLException
	 */
	void sendMailMessage(List<RecipientDTO> recipients, String subject, String messageText, List<AttachmentDTO> attachments,
			String from) throws MessagingException, SQLException;

}