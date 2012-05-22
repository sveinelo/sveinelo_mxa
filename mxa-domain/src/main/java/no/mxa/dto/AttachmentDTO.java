/*
 * #%L
 * Domain
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
package no.mxa.dto;

import java.io.Serializable;
import java.sql.Clob;

/**
 * DTO class which handles database mappings related to message attachments to be stored in the repository
 */
public class AttachmentDTO implements Serializable {

	/**
	 * Unique generated version number
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Private class variables
	 */
	private Long id;
	private MessageDTO message;
	// TODO Clob, remove this field, and only use attachmentAsString. Map java.lang.String to database Clob field
	private Clob attachment;
	private String mimeType;
	private String fileName;
	private String name;
	private String attachmentAsString;

	/**
	 * Default constructor
	 */
	public AttachmentDTO() {

	}

	/**
	 * Constructor which takes message attachment content as parameters
	 * 
	 * @param attachment
	 *            The base64 encoded attachment
	 * @param mimeType
	 *            The attachment mime type
	 * @param fileName
	 *            The attachment file name
	 * @param name
	 *            The attachment name
	 * @param attachmentAsString
	 *            The CLOB as a String object
	 */
	public AttachmentDTO(Clob attachment, String mimeType, String fileName, String name, String attachmentAsString) {
		this.attachment = attachment;
		this.mimeType = mimeType;
		this.fileName = fileName;
		this.name = name;
		this.attachmentAsString = attachmentAsString;
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
	 *            : The id value to set on the message
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
	 *            : The parent of the attachment object
	 */
	public void setMessage(MessageDTO message) {
		this.message = message;
	}

	/**
	 * Getter method for attachment
	 * 
	 * @return: attachment
	 */
	public Clob getAttachment() {
		return attachment;
	}

	/**
	 * Setter method for attachment
	 * 
	 * @param attachment
	 *            : The attachment as a Clob object
	 */
	public void setAttachment(Clob attachment) {
		this.attachment = attachment;
	}

	/**
	 * Getter method for mimeType
	 * 
	 * @return: mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Setter method for mimeType
	 * 
	 * @param mimeType
	 *            : The mimeType of the attachment
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Getter method for fileName
	 * 
	 * @return: fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setter method for fileName
	 * 
	 * @param fileName
	 *            : The file name of the attachment
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Getter method for name
	 * 
	 * @return: name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for name
	 * 
	 * @param name
	 *            : The name of the attachment
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for attachmentAsString
	 * 
	 * @return: attachmentAsString
	 */
	public String getAttachmentAsString() {
		assert (attachmentAsString != null) : "attachmentAsString may not be null here";
		return attachmentAsString;
	}

	/**
	 * Setter method for attachmentAsString
	 * 
	 * @param attachmentAsString
	 *            : The CLOB object as a string
	 */
	public void setAttachmentAsString(String attachmentAsString) {
		this.attachmentAsString = attachmentAsString;
	}
}
