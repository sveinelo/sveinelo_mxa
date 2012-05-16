package no.mxa.service.altut;

import no.mxa.dto.MessageDTO;

/**
 * Sends a single message and commit it at the end.
 */
public interface SingleMessageSender {

	/**
	 * Sends a message to Altinn based on the given intput message
	 * 
	 * @param message
	 * @return true if sent successfully to Altinn, false otherwise.
	 * @throws MessageSendException
	 *             thrown to abort the transaction
	 */
	boolean sendMessage(MessageDTO message) throws MessageSendException;

}