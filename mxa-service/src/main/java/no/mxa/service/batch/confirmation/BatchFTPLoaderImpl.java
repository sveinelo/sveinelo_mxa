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
package no.mxa.service.batch.confirmation;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import no.mxa.UniversalConstants;
import no.mxa.dto.LogDTO;
import no.mxa.service.KeyValues;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.utils.UnicodeUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchFTPLoaderImpl implements BatchFTPLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchFTPLoaderImpl.class);
	public static final String PROCESSED_FOLDER_NAME = "processed/";
	private final KeyValues keyValues;
	private final LogGenerator logGenerator;
	private final LogService logService;

	public BatchFTPLoaderImpl(KeyValues keyValues, LogGenerator logGenerator, LogService logService) {
		this.keyValues = keyValues;
		this.logGenerator = logGenerator;
		this.logService = logService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.batch.confirmation.BatchFTPLoader#loadFilesFromFTP()
	 */
	@Override
	public Map<String, String> getFilesFromFtpAsString() throws MxaFtpException {

		FTPClient client = null;
		try {
			client = createFtpClientWithCorrectRootFolder();
			FTPFile[] ftpFiles = listFilesInCurrentDirectory(client);
			return putFilesContentsInMapAsStrings(client, ftpFiles);
		} finally {
			if (client != null && client.isConnected()) {
				try {
					client.logout();
					client.disconnect();
				} catch (IOException ioe) {
					LOGGER.debug("Failed to disconnect.", ioe);
				}
			}
		}
	}

	private FTPClient createFtpClientWithCorrectRootFolder() throws MxaFtpException {
		FTPClient client = new FTPClient();
		try {
			client.connect(keyValues.getReceiptFtpServer());

			boolean login = client.login(keyValues.getReceiptFtpUser(), keyValues.getReceiptFtpPassword());
			boolean changeWorkingDirectory = client.changeWorkingDirectory(keyValues.getReceiptFtpPath());

			if (login && changeWorkingDirectory) {
				return client;
			} else {
				throw new MxaFtpException("Failed to login and/or change working directory.");
			}
		} catch (SocketException e) {
			throw new MxaFtpException("Failed to create socket.", e);
		} catch (IOException e) {
			throw new MxaFtpException("Failed to communicate with Ftp.", e);
		}
	}

	private FTPFile[] listFilesInCurrentDirectory(FTPClient client) throws MxaFtpException {
		try {
			return client.listFiles();
		} catch (IOException e) {
			throw new MxaFtpException("Failed to list files", e);
		}
	}

	private Map<String, String> putFilesContentsInMapAsStrings(FTPClient client, FTPFile[] ftpFiles) {
		Map<String, String> receipts = new HashMap<String, String>();

		for (FTPFile ftpFile : ftpFiles) {
			if (isRecieptFile(ftpFile)) {
				String filename = ftpFile.getName();
				LOGGER.debug("FTPFile: '{}'", filename);

				try (InputStream inputStream = client.retrieveFileStream(filename);) {
					String xml = convertStreamToString(inputStream);
					receipts.put(filename, xml);
					logThatFileHasBeenParsed(ftpFile);
				} catch (IOException e) {
					logFailureToProcessFile(ftpFile, e);
				} finally {
					try {
						// has to be run when reading FTP files as streams
						boolean completePendingCommand = client.completePendingCommand();
						if (completePendingCommand) {
							moveFileToProcessedFolder(client, filename);
						}
					} catch (IOException e) {
						// TODO: Ftp, should we also log something to the database-log?
						LOGGER.error("Ftp failure when completePendingCommand", e);
					}

				}
			}
		}
		return receipts;
	}

	private boolean isRecieptFile(FTPFile ftpFile) {
		String filename = ftpFile.getName();
		String fileSuffix = filename.lastIndexOf(".") > 0 ? filename.substring(filename.lastIndexOf(".")) : "";

		return (ftpFile.getType() == FTPFile.FILE_TYPE)
				&& (fileSuffix.equalsIgnoreCase(".xml") || fileSuffix.equalsIgnoreCase(".log"));
	}

	private String convertStreamToString(InputStream is) throws IOException {
		UnicodeUtil.UnicodeInputStream bomInputStream = new UnicodeUtil.UnicodeInputStream(is, "UTF-8");
		StringWriter sw = new StringWriter();
		IOUtils.copy(bomInputStream, sw);
		String returnValue = sw.toString();
		returnValue = UnicodeUtil.decode(returnValue, "UTF-8");
		return returnValue;
	}

	private void logThatFileHasBeenParsed(FTPFile ftpFile) {
		LogDTO logEntry = logGenerator.generateLog("Bekreftelsesfil ferdig behandlet: " + ftpFile.getName(),
				UniversalConstants.RCT_PROCESS_FILE);
		logService.saveLog(logEntry);
	}

	private void logFailureToProcessFile(FTPFile ftpFile, IOException e) {
		// XML parsing error. Log error in app db and app log.
		String simpleName = e.getClass().getSimpleName();
		LogDTO logEntry = logGenerator.generateLog("Feil under prosessering av bekreftelsesfil (" + simpleName + "): "
				+ ftpFile.getName(), UniversalConstants.RCT_PROCESS_FILE_FAILED);
		logService.saveLog(logEntry);
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Got " + simpleName + " when parsing receipt file");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Got " + simpleName + " when parsing receipt file", e);
		}
	}

	private void moveFileToProcessedFolder(FTPClient client, String filename) {
		try {
			boolean rename = client.rename(filename, PROCESSED_FOLDER_NAME + filename);
			if (!rename) {
				// TODO: Ftp, should we also log something to the database-log?
				LOGGER.error("Can not rename receipt file: " + filename);
			}
		} catch (IOException e) {
			// TODO: Ftp, should we also log something to the database-log?
			LOGGER.error("Can not rename receipt file: " + filename, e);
		}
	}

}
