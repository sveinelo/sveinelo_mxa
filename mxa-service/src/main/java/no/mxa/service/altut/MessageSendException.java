package no.mxa.service.altut;


public class MessageSendException extends Exception {
	
	private static final long serialVersionUID = 8057845055367000575L;

	public MessageSendException(String errorMessage, Exception exception) {
		super(errorMessage, exception);
	}


}
