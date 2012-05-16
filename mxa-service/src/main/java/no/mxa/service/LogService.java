package no.mxa.service;

import java.util.List;

import no.mxa.dto.LogDTO;

public interface LogService {

	/**
	 * 
	 * @param id
	 *            The id of the LogDTO to retrieve
	 * @return The matching log DTO
	 */
	LogDTO searchById(Long id);

	/**
	 * 
	 * @param propertyName
	 *            The name of the property, e.g messageId
	 * @param value
	 *            The value of the property
	 * @return A list of logDTOs
	 */
	List<LogDTO> searchByProperty(String propertyName, Object value);

	/**
	 * 
	 * @param log
	 *            The logDTO to save via the repository
	 */
	void saveLog(LogDTO log);

	/**
	 * 
	 * @return all log entries with null value in messageId
	 */
	List<LogDTO> getAllLogsWithNullInMessageId();
}