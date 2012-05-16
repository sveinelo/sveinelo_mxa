package no.mxa.ws.webservice;

public interface IMXA {

	/**
	 * 
	 * @param message
	 *            The message to be parsed and saved in the repository
	 * @return Return code based on XML validation, parsing and repository save operation
	 */
	int submitMessage(String message);
}