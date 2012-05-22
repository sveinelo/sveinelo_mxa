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

import java.io.PrintStream;
import java.util.Set;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class SoapLoggingHandler implements SOAPHandler<SOAPMessageContext> {
	private final FilePrintStreamFactory filePrintStreamFactory;

	@Inject
	public SoapLoggingHandler(FilePrintStreamFactory filePrintStreamFactory) {
		this.filePrintStreamFactory = filePrintStreamFactory;
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		// TODO sjekke log level
		logToSystemOut(smc);
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		// TODO sjekke log levels
		logToSystemOut(smc);
		return true;
	}

	public void close(MessageContext messageContext) {
		// nothing to clean up
	}

	private void logToSystemOut(SOAPMessageContext smc) {
		PrintStream out = filePrintStreamFactory.create();

		SOAPMessage message = smc.getMessage();
		try {
			message.writeTo(out);
			out.println(""); // just to add a newline
		} catch (Exception e) {
			out.println("Exception in handler: " + e);
		} finally {
			out.close();
		}
	}

}