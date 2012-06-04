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

import java.util.List;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;
import no.mxa.service.NotUniqueMessageException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiptXMLProcessorImpl implements ReceiptXMLProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptXMLProcessorImpl.class);

	private final LogGenerator logGenerator;
	private final LogService logService;
	private final MessageService messageService;
	private final List<ReceiptAdapter> receiptAdapters;

	@Inject
	public ReceiptXMLProcessorImpl(LogGenerator logGenerator, LogService logService, MessageService messageService,
			List<ReceiptAdapter> receiptAdapters) {
		assert (receiptAdapters != null) : "Must provide a list of ReceiptAdapters";
		this.logGenerator = logGenerator;
		this.logService = logService;
		this.messageService = messageService;
		this.receiptAdapters = receiptAdapters;
	}

	@Override
	public boolean process(String xml, String filename) {
		boolean processed = false;
		for (ReceiptAdapter adapter : receiptAdapters) {
			try {
				List<MessageAdapter> messages = adapter.parseXml(xml);
				for (MessageAdapter msg : messages) {
					if (msg.isValid()) {
						updateMessageAndCreateLog(msg);
					}
				}
				processed = true;
				// If we can decode the XML with the adapter, stop processing with other adapters.
				break;
			} catch (ValidationException e) {
				LOGGER.debug("The Message cannot be decoded with the {} adapter", new Object[] { adapter.getClass().getName(),
						e });
				LOGGER.trace("Problem while decoding", e);
			} catch (MarshalException e) {
				LOGGER.debug("The Message cannot be decoded with the {} adapter", new Object[] { adapter.getClass().getName(),
						e });
				LOGGER.trace("Problem while decoding", e);
			}
		}
		if (!processed) {
			LOGGER.warn("The Message has not been processed {} - No Adapters found for decoding it.:\n{}", filename, xml);
			// XML parsing error. Log error in app db and app log.
			LogDTO logEntry = logGenerator.generateLog("Feil under prosessering av bekreftelsesfil: " + filename,
					UniversalConstants.RCT_PROCESS_FILE_FAILED);
			logService.saveLog(logEntry);
		} else {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Processed Message: '{}'...", xml.substring(0, 50));
			}
		}
		return processed;
	}

	void updateMessageAndCreateLog(MessageAdapter msg) {
		try {
			LogEntry tempLog = msg.getLogEntry();
			LogDTO logDTO = logGenerator.generateLog(tempLog.toLogEntryMessage(), msg.getLogType(),
					tempLog.getMessageReference());
			if (logDTO.hasMessage()) {
				logService.saveLog(logDTO);
				LOGGER.debug("{} log entry persisted.", tempLog.getType());

				// Updated status on the Message.
				MessageDTO messageDTO = logDTO.getMessage();

				if (msg.isRead()) {
					messageDTO.setMessageStatus(UniversalConstants.MSG_STATUS_READ_IN_ALTINN);
				} else if (msg.isConfirmed()) {
					messageDTO.setMessageStatus(UniversalConstants.MSG_STATUS_CONFIRMED_IN_ALTINN);
				}

				messageService.mergeMessage(messageDTO);
				LOGGER.debug("MessageDTO status updated.");
			} else {
				persistLogEntryWithMessageIdZero(tempLog);
			}
		} catch (NotUniqueMessageException | InvalidLogEntryTypeException e) {
			// TODO: Can this be right? We should do something more here!!
			LOGGER.error("Error creating Message:", e);
		}
	}

	private void persistLogEntryWithMessageIdZero(LogEntry tempLog) throws InvalidLogEntryTypeException {
		// The Confirmation (NB! not Confirmed) attribute did NOT have a messageReference.
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("LogEntry does not have a messageReference TYPE:" + tempLog.getType() + " VALUE: "
					+ tempLog.toLogEntryMessage() + " messageReference: " + tempLog.getMessageReference());
		}
		LogDTO logDTO = logGenerator.generateLog(tempLog.toLogEntryMessage(), UniversalConstants.RCT_PROCESS_NO_MSG_REF);
		logService.saveLog(logDTO);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Log entry persisted with messageId=0.");
		}
	}

}
