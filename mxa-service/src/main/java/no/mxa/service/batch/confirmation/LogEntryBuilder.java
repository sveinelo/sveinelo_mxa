package no.mxa.service.batch.confirmation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEntryBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogEntryBuilder.class);
	private final LogEntryImpl logEntry;
	private State state = State.INITIALIZED;

	private enum State {
		INITIALIZED, BUILD
	}

	static class IllegalLogEntryStateException extends IllegalStateException {
		private static final long serialVersionUID = 1L;
	}

	private class LogEntryImpl implements LogEntry, Cloneable {
		// Confirmation attributes
		private String participantId;
		private String shortName;
		private String messageReference;
		// LogEntry type
		private String type; // Read of Confirmation
		// Read Entry type type attributes
		private String readDateTime;
		// Confirmed type attributes
		private String confirmedDateTime;
		private String confirmedRoleList;
		private String loginMethod;

		/**
		 * If the <LogEntry> type is "Read", attributes concerning a Read entry will be returned. And similar for "Confirmed"
		 * type.
		 * 
		 * @return A String representation of the object based on it's type
		 */
		@Override
		public String toLogEntryMessage() throws InvalidLogEntryTypeException {
			if (type == null) {
				throw new InvalidLogEntryTypeException();
			} else if (type.equals("Read")) {
				return "participantId: " + participantId + " readDateTime: " + readDateTime + " shortName: " + shortName;
			} else if (type.equals("Confirmed")) {
				return "participantId: " + participantId + " shortName: " + shortName + " confirmedDateTime: "
						+ confirmedDateTime + " confirmedRoleList " + confirmedRoleList + " loginMethod: " + loginMethod;
			} else {
				throw new InvalidLogEntryTypeException();
			}
		}

		/**
		 * Creates a new object which is <CODE>equals()</CODE> to this object, but not <CODE>==</CODE>.
		 * 
		 * @return new cloned instance of this object
		 */
		@Override
		protected Object clone() throws CloneNotSupportedException {
			LogEntryImpl clonedLog = new LogEntryImpl();
			clonedLog.setMessageReference(messageReference);
			clonedLog.setParticipantId(participantId);
			clonedLog.setReadDateTime(readDateTime);
			clonedLog.setShortName(shortName);
			clonedLog.setType(type);
			clonedLog.setConfirmedDateTime(confirmedDateTime);
			clonedLog.setConfirmedRoleList(confirmedRoleList);
			clonedLog.setLoginMethod(loginMethod);
			return clonedLog;
		}

		@Override
		public String toString() {
			return "LogEntry [participantId=" + getParticipantId() + ", shortName=" + getShortName() + ", messageReference="
					+ getMessageReference() + ", type=" + getType() + ", readDateTime=" + getReadDateTime()
					+ ", confirmedDateTime=" + getConfirmedDateTime() + ", confirmedRoleList=" + getConfirmedRoleList()
					+ ", loginMethod=" + getLoginMethod() + "]";
		}

		@Override
		public String getParticipantId() {
			return participantId;
		}

		/**
		 * Setter for the ID of the Confirming User
		 * 
		 * @param participantId
		 */
		public void setParticipantId(String participantId) {
			this.participantId = participantId;
		}

		@Override
		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortName) {
			this.shortName = shortName;
		}

		@Override
		public String getMessageReference() {
			return messageReference;
		}

		public void setMessageReference(String messageReference) {
			this.messageReference = messageReference;
		}

		@Override
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public String getReadDateTime() {
			return readDateTime;
		}

		public void setReadDateTime(String readDateTime) {
			this.readDateTime = readDateTime;
		}

		@Override
		public String getConfirmedDateTime() {
			return confirmedDateTime;
		}

		public void setConfirmedDateTime(String confirmedDateTime) {
			this.confirmedDateTime = confirmedDateTime;
		}

		@Override
		public String getConfirmedRoleList() {
			return confirmedRoleList;
		}

		public void setConfirmedRoleList(String confirmedRoleList) {
			this.confirmedRoleList = confirmedRoleList;
		}

		@Override
		public String getLoginMethod() {
			return loginMethod;
		}

		public void setLoginMethod(String loginMethod) {
			this.loginMethod = loginMethod;
		}
	}

	public LogEntryBuilder(String type) {
		logEntry = new LogEntryImpl();
		logEntry.setType(type);
	}

	public LogEntry build() {
		try {
			state = State.BUILD;
			return (LogEntry) logEntry.clone();
		} catch (CloneNotSupportedException e) {
			LOGGER.error("Error while cloning the instance. This should never happen.", e);
			throw new RuntimeException(e);
		}
	}

	public LogEntryBuilder setMessageReference(String correspondenceReference) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setMessageReference(correspondenceReference);
		return this;
	}

	public LogEntryBuilder setShortName(String string) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setShortName(string);
		return this;
	}

	public LogEntryBuilder setReadDateTime(String format) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setReadDateTime(format);
		return this;
	}

	public LogEntryBuilder setConfirmedRoleList(String confirmedRoleList) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setConfirmedRoleList(confirmedRoleList);
		return this;
	}

	public LogEntryBuilder setConfirmedDateTime(String confirmedDateTime) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setConfirmedDateTime(confirmedDateTime);
		return this;
	}

	public LogEntryBuilder setParticipantId(String userSSN) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setParticipantId(userSSN);
		return this;

	}

	public LogEntryBuilder setLoginMethod(String loginMethod) {
		if (state == State.BUILD)
			throw new IllegalLogEntryStateException();
		logEntry.setLoginMethod(loginMethod);
		return this;

	}

}
