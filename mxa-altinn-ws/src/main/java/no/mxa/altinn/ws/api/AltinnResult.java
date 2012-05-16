package no.mxa.altinn.ws.api;

/**
 * Class used for result of parsing result XML from Altinn
 * 
 * 
 */
public class AltinnResult {
	private final String result;
	private final String messageEntry;

	public AltinnResult(String result, String messageEntry) {
		this.result = result;
		this.messageEntry = messageEntry;
	}
	
	public String getResult() {
		return result;
	}

	public String getMessageEntry() {
		return messageEntry;
	}

}
