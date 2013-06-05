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
package no.mxa.ws.webservice;

import java.io.BufferedWriter;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.MessageService;
import no.mxa.ws.parser.DTOGenerator;
import no.mxa.ws.parser.Message;
import no.mxa.ws.parser.Parser;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MXA implements IMXA {
	private static final Logger LOGGER = LoggerFactory.getLogger(MXA.class);

	private final Parser parser;
	private final DTOGenerator dtoGenerator;
	private final MessageService messageService;
	private final LogGenerator logGenerator;

	private boolean throwExceptions = false;

	@Inject
	public MXA(Parser parser, DTOGenerator dtoGenerator, MessageService messageService, LogGenerator logGenerator) {
		this.parser = parser;
		this.dtoGenerator = dtoGenerator;
		this.messageService = messageService;
		this.logGenerator = logGenerator;
	}

	/**
	 * 1 - Validate message 2 - Parse message 3 - Save message 4 - Return return code based on steps 1 - 3
	 */
	@Override
	// TODO: Refactor this method.
	public int submitMessage(final String msg) {
		assert (msg != null) : "Message may not be null.";
		int returnCode = -1;
		try {
			if (LOGGER.isTraceEnabled()) {
				writeXMLfile("# Original string:\n" + msg);
			}

			String message = handleByteOrderMarkAndCharacterEncoding(msg);

			if (LOGGER.isTraceEnabled()) {
				writeXMLfile("# Parsed string:\n" + message);
			}

			LOGGER.debug("submitMessage start");

			// Run the XML validation
			String validationString = parser.validateDocument(message);

			LOGGER.debug("Validation String: {}", validationString);

			Message parsedMessage = null;
			// If validation is OK, run the XML parsing
			if (validationString.equals("OK")) {
				parsedMessage = parser.parseDocument(message);
			} else {

				// FIXME This assignment will never have any effect since it WILL be overwritten later. Maybe we should abort
				// processing here...
				returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_VALIDATION_ERROR;

				// Save log related to message parsing error
				logGenerator.saveLog(UniversalConstants.MSG_PARSE_ERROR_DESCRIPTION, UniversalConstants.MSG_PARSE_ERROR);
			}

			boolean saved = false;
			// If parsing is OK, run the repository save
			if (parsedMessage != null) {
				LOGGER.debug("Save message");
				MessageDTO messageDTO = dtoGenerator.generateMessageDTO(parsedMessage);
				try {
					messageService.saveMessage(messageDTO);
					saved = true;
				} catch (RuntimeException re) {
					saved = false;
					LOGGER.debug("Saving message failed: ", re);
				}
			} else {
				// FIXME This assignment will never have any effect since it WILL be overwritten later. Maybe we should abort
				// processing here...
				returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_VALIDATION_ERROR;
			}

			if (saved) {
				returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_SAVED;
			} else if (!saved && returnCode == -1) {
				// If saving is not OK, return 1
				returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_SAVING_ERROR;

				// Save log related to message parsing error
				logGenerator.saveLog(UniversalConstants.MSG_SAVE_ERROR_DESCRIPTION, UniversalConstants.MSG_SAVE_ERROR);
			}

			// Else return 2
		} catch (RuntimeException re) {
			// Important to never let any exceptions be visible to the client.
			LOGGER.error("Failed", re);
			returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_SAVING_ERROR;
			if (isThrowExceptions()) {
				throw re;
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * @param originalMessage
	 *            message as it was when received
	 * @return message with byte order mark removed if present
	 */
	private String handleByteOrderMarkAndCharacterEncoding(final String originalMessage) {
		// TODO: What if we recieve ISO-8859-1 from the client? Then æøå will not be handled correctly.

		// Remove Byte Order Mark from XML if present. The XML-parser fails if BOM is present.
		if (originalMessage.indexOf("<") > 0 && originalMessage.indexOf("<") < 4) {
			return originalMessage.substring(originalMessage.indexOf("<"));
		}
		return originalMessage;

		// return UnicodeUtil.decode(msg, "UTF-8");
	}

	private void writeXMLfile(String message) {
		String generatedFilename = System.currentTimeMillis() + ".xml";
		try (FileWriterWithEncoding fileWriterWithEncoding = new FileWriterWithEncoding(generatedFilename, "UTF-8");
				BufferedWriter out = new BufferedWriter(fileWriterWithEncoding);) {
			out.write(message);
		} catch (Exception e) {
			LOGGER.debug("Can not write xml file", e);
			LOGGER.debug(message);
		}
	}

	public boolean isThrowExceptions() {
		return throwExceptions;
	}

	public void setThrowExceptions(boolean throwExceptions) {
		this.throwExceptions = throwExceptions;
	}
}
