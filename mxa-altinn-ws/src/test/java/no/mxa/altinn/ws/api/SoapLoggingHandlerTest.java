package no.mxa.altinn.ws.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import no.mxa.UniversalConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SoapLoggingHandlerTest {
	@Mock
	private FilePrintStreamFactory filePrintStreamFactory;
	private SoapLoggingHandler loggingHandler;

	@Before
	public void setup() {
		loggingHandler = new SoapLoggingHandler(filePrintStreamFactory);
	}

	@Test
	public void shouldDoNothing() {
		MessageContext messageContext = mock(MessageContext.class);
		loggingHandler.close(messageContext);
		verifyZeroInteractions(messageContext);
	}

	@Test
	public void shouldReturnNull() throws Exception {
		Set<QName> headers = loggingHandler.getHeaders();
		assertTrue(headers == null);
	}

	@Test
	public void shouldNotThrowExceptionWhenThereIsNoMessageInContext() throws FileNotFoundException {
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		PrintStream printStream = mock(PrintStream.class);

		when(filePrintStreamFactory.create()).thenReturn(printStream);

		boolean handleMessage = loggingHandler.handleMessage(smc);

		assertTrue("handleMessage should always return true", handleMessage);
	}

	@Test
	public void shouldContainALineSeparatorAtEndOfFile() throws SOAPException, IOException {
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		SOAPMessage soapMessage = mock(SOAPMessage.class);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		when(filePrintStreamFactory.create()).thenReturn(new PrintStream(out, true));
		when(smc.getMessage()).thenReturn(soapMessage);

		boolean handleMessage = loggingHandler.handleMessage(smc);

		verify(soapMessage, times(1)).writeTo(any(OutputStream.class));
		assertTrue("handleMessage should always return true", handleMessage);
		assertThat(out.toString(UniversalConstants.CHARACTER_SET_UTF8), is(System.lineSeparator()));
	}

	@Test
	public void shouldCloseStream() {
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		SOAPMessage soapMessage = mock(SOAPMessage.class);
		PrintStream printStream = mock(PrintStream.class);

		when(filePrintStreamFactory.create()).thenReturn(printStream);
		when(smc.getMessage()).thenReturn(soapMessage);

		loggingHandler.handleMessage(smc);

		verify(printStream).close();
	}
}
