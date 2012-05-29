package no.mxa.utils;

import java.io.IOException;

public class BOMException extends RuntimeException {

	private String message = "Problem handling BOM in UTF test";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BOMException(IOException e) {
		super(e);
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
