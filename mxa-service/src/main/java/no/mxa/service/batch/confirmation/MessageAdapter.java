package no.mxa.service.batch.confirmation;

import no.mxa.UniversalConstants.LogType;

public interface MessageAdapter {

	boolean isValid();

	/**
	 * creates a LogDTO Object from the Message
	 * 
	 * @return the Message as LogDTO object
	 */
	LogEntry getLogEntry();

	boolean isConfirmed();

	boolean isRead();

	LogType getLogType();
}
