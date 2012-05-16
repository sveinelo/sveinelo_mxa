package no.mxa.service.batch.notification;

import javax.mail.MessagingException;

public interface SendNoticesAndWarnings {

	/**
	 * Sends overdue-notices and warnings. * OverdueNotice = readDeadline > today and overdueNotice not sent. Warning =
	 * readDeadline > today and overdueNotice sent.
	 * 
	 * OverdueNotice is sent after seven days, warning is sent after 14 days.
	 * 
	 * Warning is not the best word. It is the complete message with attachments sent to a specified e-mail, from keyValues,
	 * where a case officer can manually take care of the message. (For example send it by snail-mail.)
	 * 
	 * @throws MessagingException
	 */
	void start() throws MessagingException;
}