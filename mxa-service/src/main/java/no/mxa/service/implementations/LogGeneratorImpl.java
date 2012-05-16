package no.mxa.service.implementations;

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;

import no.mxa.UniversalConstants.LogType;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;
import no.mxa.service.NotUniqueMessageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogGeneratorImpl implements LogGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogGeneratorImpl.class);

	@Inject
	private LogService logService;
	@Inject
	// TODO: There is a circular reference between LogGenerator and MessageService
	private MessageService messageService;

	private LogDTO logDTO;

	/**
	 * Generates new log entries that are not connected to a message.
	 */
	@Override
	public LogDTO generateLog(String logMessage, String logType) {
		// Do not connect to a message
		MessageDTO messageDTO = null;
		Timestamp time = new Timestamp(System.currentTimeMillis());
		// Generate LogDTO object
		return generateLog(logMessage, logType, messageDTO, time);
	}

	/**
	 * Generates new log entries to a message object, variant that takes messageId as input
	 */
	@Override
	public LogDTO generateLog(String logMessage, String logType, long messageId) {
		// Get the related message
		MessageDTO messageDTO = messageService.searchById(messageId);
		Timestamp time = new Timestamp(System.currentTimeMillis());
		// Generate LogDTO object
		return generateLog(logMessage, logType, messageDTO, time);
	}

	/**
	 * Generates new log entries to a message object, variant that takes messageId and timestamp as input
	 */
	@Override
	public LogDTO generateLog(String logMessage, String logType, long messageId, Timestamp time) {
		// Get the related message
		MessageDTO messageDTO = messageService.searchById(messageId);
		// Generate LogDTO object
		return generateLog(logMessage, logType, messageDTO, time);
	}

	/**
	 * Generates new log entries to a message object, variant that takes messageReference as input
	 */
	@Override
	public LogDTO generateLog(String logMessage, String logType, String messageReference) throws NotUniqueMessageException {
		LOGGER.debug("Generating log with messageReference:" + messageReference);
		// Get messageDTO
		List<MessageDTO> messageDTOList = messageService.searchByProperty("messageReference", messageReference);
		if (messageDTOList.size() != 1) {
			LOGGER.debug("Failed to generate logg with messageReference:" + messageReference);
			throw new NotUniqueMessageException();
		}
		LOGGER.debug("LogDTO genration success.");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		return generateLog(logMessage, logType, messageDTOList.get(0), time);
	}

	/**
	 * 
	 * @param logMessage
	 *            The log message
	 * @param logType
	 *            The log type
	 * @param messageDTO
	 *            The parent message, if it exists
	 * @return The log as a DTO object
	 */
	private LogDTO generateLog(String logMessage, String logType, MessageDTO messageDTO, Timestamp time) {
		// Create a new logDTO
		logDTO = new LogDTO();

		// Set logDTO values
		logDTO.setLogMessage(logMessage);
		logDTO.setTime(time);
		logDTO.setLogType(logType);

		// Set parent if parent != null
		if (messageDTO != null) {
			logDTO.setMessage(messageDTO);
		}

		// Returns a log DTO
		return logDTO;
	}

	/**
	 * Saves log messages in the repository
	 */
	@Override
	public void saveLog(LogDTO log) {
		logService.saveLog(log);
	}

	@Override
	public LogDTO generateLog(String logEntryMessage, LogType logType, String messageReference)
			throws NotUniqueMessageException {
		if (logType != null)
			return generateLog(logEntryMessage, logType.toString(), messageReference);
		else
			return generateLog(logEntryMessage, "", messageReference);
	}
}
