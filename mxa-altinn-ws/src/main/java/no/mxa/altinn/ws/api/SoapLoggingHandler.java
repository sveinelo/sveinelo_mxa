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