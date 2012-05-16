package no.mxa.service.batch.confirmation;

/**
 * This class i used to hold data when parsing a confirmation xml. The object is populated during the parsing, and should be
 * reset at the appropriate time. There is also a convenient toLogEntryMessage method which return a string. The string should
 * be saved as part of a LogDTO.
 * 
 */
public interface LogEntry {

	/**
	 * If the <LogEntry> type is "Read", attributes concerning a Read entry will be returned. And similar for "Confirmed" type.
	 * 
	 * @return A String representation of the object based on it's type
	 */
	String toLogEntryMessage() throws InvalidLogEntryTypeException;

	/**
	 * Reset all attributes except participantId, shortName and messageReference (eg. confirmation attributes.) since they might
	 * be needed by the next logEntry.
	 */

	String getParticipantId();

	String getShortName();

	String getMessageReference();

	String getType();

	String getReadDateTime();

	String getConfirmedDateTime();

	String getConfirmedRoleList();

	String getLoginMethod();

}
