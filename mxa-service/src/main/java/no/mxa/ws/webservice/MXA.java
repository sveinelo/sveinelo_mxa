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
package no.mxa.ws.webservice;

import java.io.BufferedWriter;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.dto.LogDTO;
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

	private Parser parser;
	private DTOGenerator dtoGenerator;
	private MessageService messageService;
	private LogGenerator logGenerator;

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
	public int submitMessage(String message) {
		assert (message != null) : "Message may not be null.";
		int returnCode;
		boolean saved;
		returnCode = -1;
		String validationString = null;
		Message parsedMessage = null;
		MessageDTO messageDTO = null;
		parsedMessage = null;
		saved = false;

		if (LOGGER.isTraceEnabled()) {
			writeXMLfile(message);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("submitMessage start");
		}

		// Remove Byte Order Mark from XML if present. The XML-parser fails if BOM is present.
		if (message.indexOf("<") > 0 && message.indexOf("<") < 4) {
			message = message.substring(message.indexOf("<"));
		}

		// Run the XML validation
		validationString = parser.validateDocument(message);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Validation String: " + validationString);
		}

		// If validation is OK, run the XML parsing
		if (validationString.equals("OK")) {
			parsedMessage = parser.parseDocument(message);
		} else {
			returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_VALIDATION_ERROR;

			// Save log related to message parsing error
			LogDTO validationErrorLog = null;
			validationErrorLog = logGenerator.generateLog(UniversalConstants.MSG_PARSE_ERROR_DESCRIPTION,
					UniversalConstants.MSG_PARSE_ERROR);
			logGenerator.saveLog(validationErrorLog);
		}

		// If parsing is OK, run the repository save
		if (parsedMessage != null) {
			LOGGER.debug("Save message");
			messageDTO = dtoGenerator.generateMessageDTO(parsedMessage);
			try {
				messageService.saveMessage(messageDTO);
				saved = true;
			} catch (RuntimeException re) {
				saved = false;
				LOGGER.debug("Saving message failed: ", re);
			}
		} else {
			returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_VALIDATION_ERROR;
		}

		if (saved) {
			returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_SAVED;
		} else if (!saved && returnCode == -1) {
			// If saving is not OK, return 1
			returnCode = UniversalConstants.WEB_SERVICE_MESSAGE_SAVING_ERROR;

			// Save log related to message parsing error
			LogDTO saveErrorLog = null;
			saveErrorLog = logGenerator.generateLog(UniversalConstants.MSG_SAVE_ERROR_DESCRIPTION,
					UniversalConstants.MSG_SAVE_ERROR);
			logGenerator.saveLog(saveErrorLog);
		}

		// Else return 2

		return returnCode;
	}

	private void writeXMLfile(String message) {
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriterWithEncoding(System.currentTimeMillis() + ".xml", "UTF-8"));
			out.write(message);
			out.close();
		} catch (Exception e) {
			LOGGER.debug("Can not write xml file", e);
			LOGGER.debug(message);
		}
	}
}
