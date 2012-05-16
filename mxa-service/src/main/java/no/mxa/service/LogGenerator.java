package no.mxa.service;

import java.sql.Timestamp;

import no.mxa.UniversalConstants.LogType;
import no.mxa.dto.LogDTO;

public interface LogGenerator {

	/**
	 * Method used to generate a LogDTO that is not connected to a Message
	 * 
	 * @param additionalLogMessage
	 *            : String to eventually append to the regular log message
	 * @param type
	 *            : Log type. See definitions in UniversalConstants
	 * @return A LogDTO object to be saved in the repository
	 */
	LogDTO generateLog(String additionalLogMessage, String type);

	/**
	 * Method used to generate a LogDTO that is connected to a message with messageId
	 * 
	 * @param additionalLogMessage
	 *            : String to eventually append to the regular log message
	 * @param type
	 *            : Log type. See definitions in UniversalConstants
	 * @param messageId
	 *            : The ID of the related message
	 * @return A LogDTO object to be saved in the repository
	 */
	LogDTO generateLog(String additionalLogMessage, String type, long messageId);

	/**
	 * Method used to generate a LogDTO that is connected to a message with messageId and has timestamp to be saved as input.
	 * Useful for logging the same timestamp i different tables.
	 * 
	 * @param additionalLogMessage
	 *            : String to eventually append to the regular log message
	 * @param type
	 *            : Log type. See definitions in UniversalConstants
	 * @param messageId
	 *            : The ID of the related message
	 * @param time
	 *            : The timestamp to be stored in the log
	 * @return A LogDTO object to be saved in the repository
	 */
	LogDTO generateLog(String additionalLogMessage, String type, long messageId, Timestamp time);

	/**
	 * Method used to generate a LogDTO that is connected to a message with messageReference
	 * 
	 * @param additionalLogMessage
	 *            : String to eventually append to the regular log message
	 * @param type
	 *            : Log type. See definitions in UniversalConstants
	 * @param messageReference
	 *            : MessageReference of the related message
	 * @return A LogDTO object to be saved in the repository
	 */
	LogDTO generateLog(String additionalLogMessage, String type, String messageReference) throws NotUniqueMessageException;

	/**
	 * Saves log messages in the repository
	 * 
	 * @param log
	 *            The LogDTO object to be saved in the repository
	 */
	void saveLog(LogDTO log);

	LogDTO generateLog(String logEntryMessage, LogType logType, String messageReference) throws NotUniqueMessageException;
}
