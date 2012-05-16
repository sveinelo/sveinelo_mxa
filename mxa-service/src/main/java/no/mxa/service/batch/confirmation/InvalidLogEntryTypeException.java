package no.mxa.service.batch.confirmation;

/**
 * This exception is thrown when someone tries to call toLogEntryMessage() on a LogEntry with invalid type.
 */
public class InvalidLogEntryTypeException extends Exception {
	private static final long serialVersionUID = 5399878607685645424L;
}
