package no.mxa.service.altut;

import java.util.List;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to send messages to Altinn using web service.
 * 
 */
public class MessageSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

	private SingleMessageSender singleMessageSender;
	private MessageService messageService;
	private LogGenerator logGenerator;
	private LogService logService;

	@Inject
	public MessageSender(SingleMessageSender singleMessageSender, MessageService messageService, LogGenerator logGenerator,
			LogService logService) {
		this.singleMessageSender = singleMessageSender;
		this.messageService = messageService;
		this.logGenerator = logGenerator;
		this.logService = logService;
	}

	/**
	 * This method is called from a quartzjob to send new messages to Altinn.
	 * 
	 */
	public synchronized String sendMessages() {
		AccumulatedResult acummulatedResult = new AccumulatedResult();

		for (MessageDTO message : findMessages()) {
			try {
				if (singleMessageSender.sendMessage(message)) {
					acummulatedResult.addSucceed();
				} else {
					acummulatedResult.addFailed();
				}
			} catch (Exception ex) {
				handleException(acummulatedResult, message, ex);
			}
		}
		return createResultStringBasedOn(acummulatedResult);
	}

	/**
	 * Find messages to send, messages with sentAltinn = false and messageStatus = MSG_STATUS_RECEIVED
	 * 
	 * @return
	 */
	private List<MessageDTO> findMessages() {
		MessageDTO criteria = new MessageDTO();
		criteria.setMessageStatus(UniversalConstants.MSG_STATUS_RECEIVED);
		criteria.setSentAltinn(UniversalConstants.MSG_SENTALTINN_FALSE);
		return messageService.searchByCriteria(criteria);
	}

	private void handleException(AccumulatedResult acummulatedResult, MessageDTO message, Exception ex) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Got exception when sending to altinn", ex);
		}
		LogDTO logEntry = logGenerator.generateLog("Feil ved sending til Altinn", UniversalConstants.MSG_FAILED_ALTINN,
				message.getId());
		logService.saveLog(logEntry);
		acummulatedResult.addException();
	}

	private String createResultStringBasedOn(AccumulatedResult acummulatedResult) {
		if (acummulatedResult.getFailed() == 0 && acummulatedResult.getException() == 0) {
			return "OK: Succeded: " + acummulatedResult.getSucceeded();
		} else {
			return "Failed: Succeeded:" + acummulatedResult.getSucceeded() + ", Failed: " + acummulatedResult.getFailed()
					+ ", Exception: " + acummulatedResult.getException();
		}
	}
}