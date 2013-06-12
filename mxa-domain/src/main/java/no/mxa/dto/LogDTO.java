/*
 * #%L
 * Domain
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
package no.mxa.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO class which handles database mappings related to log entries
 */
public class LogDTO implements Serializable {

	/**
	 * Unique generated version number
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Private class variables
	 */
	private Long id;
	private MessageDTO message;
	private Timestamp time;
	private String logType;
	private String logMessage;

	/**
	 * Default constructor
	 */
	public LogDTO() {

	}

	/**
	 * Constructor which takes log content as parameters
	 * 
	 * @param time
	 *            Time stamp of the log entry creation
	 * @param logMessage
	 *            The log entry description
	 */
	public LogDTO(Timestamp time, String logMessage) {
		this.time = time;
		this.logMessage = logMessage;
	}

	/**
	 * Getter method for id
	 * 
	 * @return: id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for id
	 * 
	 * @param id
	 *            : The id value to set on the log entry
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for message
	 * 
	 * @return: message
	 */
	public MessageDTO getMessage() {
		return message;
	}

	/**
	 * Setter method for message
	 * 
	 * @param message
	 *            : The log entry's parent message
	 */
	public void setMessage(MessageDTO message) {
		this.message = message;
	}

	/**
	 * Getter method for time
	 * 
	 * @return: time
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * Setter method for time
	 * 
	 * @param time
	 *            : The timestamp of the log entry creation
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}

	/**
	 * Getter method for logTypeStr
	 * 
	 * @return logTypeStr
	 */
	public String getLogType() {
		return logType;
	}

	/**
	 * Setter method for LogTypeStr
	 * 
	 * @param logTypeStr
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}

	/**
	 * Getter method for log message
	 * 
	 * @return: logMessage
	 */
	public String getLogMessage() {
		return logMessage;
	}

	/**
	 * Setter method for log message
	 * 
	 * @param logMessage
	 *            : The log entry description
	 */
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	public boolean hasMessage() {
		return message != null;
	}
}
