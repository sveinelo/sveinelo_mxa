package no.mxa.web.model;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import no.mxa.dto.LogDTO;
import no.mxa.service.LogService;

/**
 * Provides a list with all log entries which do not have any reference to a message. (messageId=0)
 */
public class LogEntriesWithNoMessageModel {
	private LogService logService;
	private List<LogDTO> logEntriesWithNoMessage;

	@Inject
	public LogEntriesWithNoMessageModel(LogService logService) {
		this.logService = logService;
	}

	@PostConstruct
	public void populateLogEntriesWithNoMessage() {
		logEntriesWithNoMessage = logService.getAllLogsWithNullInMessageId();
	}

	public List<LogDTO> getLogEntriesWithNoMessage() {
		return logEntriesWithNoMessage;
	}

}
