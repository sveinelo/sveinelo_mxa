package no.mxa.service.batch.confirmation;

public class MxaFtpException extends Exception {
	private static final long serialVersionUID = 2362246404165749404L;

	public MxaFtpException(String message, Exception e) {
		super(message, e);
	}

	public MxaFtpException(String message) {
		super(message);
	}

}
