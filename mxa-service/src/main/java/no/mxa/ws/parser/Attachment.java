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
package no.mxa.ws.parser;

import java.sql.Clob;

import org.hibernate.Hibernate;

/**
 * Class for storing attachments
 */
public class Attachment {

	private String filename;
	private String name;
	private String mimeType;
	private char[] attachment;

	public Attachment() {

	}

	public Attachment(String filename, String name, String mimeType) {
		this.filename = filename;
		this.name = name;
		this.mimeType = mimeType;

	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Clob getAttachment() {
		return Hibernate.createClob(String.valueOf(this.attachment));
	}

	public void setAttachment(char[] attachment) {
		this.attachment = attachment;
	}

}
