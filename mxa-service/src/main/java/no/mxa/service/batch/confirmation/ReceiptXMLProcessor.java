package no.mxa.service.batch.confirmation;

public interface ReceiptXMLProcessor {

	/**
	 * Parses the xml and updates message with new status based on the confirmation xml.
	 * 
	 * @param xml
	 *            string representation of the xml to process
	 * @param filename
	 *            original filename of the file the xml comes from
	 * @return true if process succeeds, false otherwise
	 */
	boolean process(String xml, String filename);

}
