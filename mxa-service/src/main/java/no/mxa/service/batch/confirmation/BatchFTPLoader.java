package no.mxa.service.batch.confirmation;

import java.util.Map;

public interface BatchFTPLoader {

	Map<String, String> getFilesFromFtpAsString() throws MxaFtpException;

}