/*
 * #%L
 * Altinn Webservice
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
package no.mxa.altinn.ws.api;

import java.io.OutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import no.mxa.utils.LoggingOutputStream;
import no.mxa.utils.LoggingOutputStream.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoapLoggingHandler implements SOAPHandler<SOAPMessageContext> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SoapLoggingHandler.class);
	private boolean throwRuntimeExceptions = false;

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		logToLogger(smc);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		logToLogger(smc);
		return true;
	}

	@Override
	public void close(MessageContext messageContext) {
		// nothing to clean up
	}

	private void logToLogger(SOAPMessageContext smc) {
		try (OutputStream out = new LoggingOutputStream(LOGGER, Level.TRACE);) {
			SOAPMessage message = smc.getMessage();
			if (message != null) {
				message.writeTo(out);
			} else {
				LOGGER.debug("No Message to Log");
			}
		} catch (Exception e) {
			LOGGER.warn("Exception in handler: ", e);
			if (throwRuntimeExceptions) {
				throw new SoapLoggingHandlerExeption(e);
			}
		}
	}

	public void setThrowRuntimeExceptions() {
		throwRuntimeExceptions = true;
	}

	public void setDoNotThrowRuntimeExceptions() {
		throwRuntimeExceptions = false;
	}
}