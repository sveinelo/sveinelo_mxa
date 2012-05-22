/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2012 Patentstyret
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
