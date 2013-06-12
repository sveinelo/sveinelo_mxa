/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package no.mxa.service.batch.confirmation;

import java.text.MessageFormat;

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
		private String reportee;

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
				return MessageFormat.format("reportee: {0}; readDateTime: {1}; shortName: {2};", reportee, readDateTime,
						shortName);
			} else if (type.equals("Confirmed")) {
				return MessageFormat
						.format("reportee: {5}; participantId: {0}; shortName: {1}; confirmedDateTime: {2}; confirmedRoleList {3}; loginMethod: {4};",
								participantId, shortName, confirmedDateTime, confirmedRoleList, loginMethod, reportee);
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

		public void setReportee(String reportee) {
			this.reportee = reportee;
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
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setMessageReference(correspondenceReference);
		return this;
	}

	public LogEntryBuilder setShortName(String string) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setShortName(string);
		return this;
	}

	public LogEntryBuilder setReadDateTime(String format) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setReadDateTime(format);
		return this;
	}

	public LogEntryBuilder setConfirmedRoleList(String confirmedRoleList) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setConfirmedRoleList(confirmedRoleList);
		return this;
	}

	public LogEntryBuilder setConfirmedDateTime(String confirmedDateTime) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setConfirmedDateTime(confirmedDateTime);
		return this;
	}

	public LogEntryBuilder setParticipantId(String userSSN) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setParticipantId(userSSN);
		return this;

	}

	public LogEntryBuilder setLoginMethod(String loginMethod) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setLoginMethod(loginMethod);
		return this;

	}

	public LogEntryBuilder setReportee(String reportee) {
		if (state == State.BUILD) {
			throw new IllegalLogEntryStateException();
		}
		logEntry.setReportee(reportee);
		return this;

	}

}
