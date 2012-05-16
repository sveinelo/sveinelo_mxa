package no.mxa.service;

import no.mxa.dto.MessageDTO;

/**
 * The two first methods are used by SendNoticesAndWarnings.java which is part of a daily quartz-triggered job. The last GUI
 * method is used by the front-end ("administrasjons-modul").
 */
public interface SendNoticeOrWarningService {
	/**
	 * This method is intended to be used for all messages which has not been read within the first seven days after
	 * readDeadline. It sends an overdue notice to all e-mail addresses in the contactInfo list of the given message.
	 * 
	 * This method adds seven days to readDeadline which gives the contact(s) seven more days to read the message in Altinn. It
	 * also updates overdueNoticeSent flag.
	 * 
	 * @param messageDTO
	 *            readDeadline > today, overdueNoticeSent==0
	 */
	void sendNoticeMail(MessageDTO messageDTO);

	/**
	 * This methods send an e-mail with all attachments to an e-mail address specified in keyValues (from the database). This
	 * method should be called with a messageDTO where overdueNotice is sent and readDeadline > today.
	 * 
	 * In practice this method is called on massages which as not been read within 14 days after MXA has successfully sent it to
	 * Altinn.
	 * 
	 * It changes the messageStatus of the message to <code>UniversalConstants.MSG_STATUS_OVERDUEWARN_SENT_TO_AGENCY</code>
	 * 
	 * @param messageDTO
	 *            readDeadline > today, overdueNoticeSent==1
	 */
	void sendWarnMail(MessageDTO messageDTO);

	/**
	 * Method intended to be used by the front-end. This method does not update anything on the given message. It only sends the
	 * complete message with attachments to a specified address from keyValues. (MAILTOPAT)
	 * 
	 * This method needs to throw an exception if we are to inform the user/front-end. As of 2009.07.02 we're not informing the
	 * user.
	 * 
	 * @param messageDTO
	 */
	void sendGUIWarnMail(MessageDTO messageDTO);
}
