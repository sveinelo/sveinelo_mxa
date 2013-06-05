/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import no.mxa.service.KeyValues;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class connects to a FTP server and processes receipt XML files from Altinn
 */
public final class ReceiptProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptProcessor.class);

	private final ReceiptXMLProcessor receiptXMLProcessor;

	private final BatchFTPLoader ftp;

	@Inject
	public ReceiptProcessor(KeyValues keyValues, ReceiptXMLProcessor receiptXMLProcessor, LogGenerator logGenerator,
			LogService logService) {
		this.receiptXMLProcessor = receiptXMLProcessor;
		ftp = new BatchFTPLoaderImpl(keyValues, logGenerator, logService);
	}

	/**
	 * Connects to a FTP server and processes receipt XML
	 */
	public void process() {
		try {
			Map<String, String> receipts = ftp.getFilesFromFtpAsString();
			processXML(receipts);
		} catch (MxaFtpException e) {
			// TODO: Ftp, handle Exceptions when communicating with FTP
			LOGGER.error("Failed to establish FTP communication", e);
		}
	}

	void processXML(Map<String, String> receipts) {
		for (Entry<String, String> receipt : receipts.entrySet()) {
			receiptXMLProcessor.process(receipt.getValue(), receipt.getKey());
		}
	}

}
