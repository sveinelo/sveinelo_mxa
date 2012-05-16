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
