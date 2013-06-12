/*
 * #%L
 * Altinn Webservice
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
package no.mxa.altinn.ws.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SoapLoggingHandlerTest {
	private SoapLoggingHandler loggingHandler;
	@Mock
	private Appender mockAppender;

	@Before
	public void setup() {
		loggingHandler = new SoapLoggingHandler();
		loggingHandler.setThrowRuntimeExceptions();
		Logger logger = Logger.getLogger(SoapLoggingHandler.class);
		logger.setLevel(Level.TRACE);
		logger.addAppender(mockAppender);
		logger.setAdditivity(false);
	}

	@Test
	public void shouldDoNothing() {
		MessageContext messageContext = mock(MessageContext.class);
		loggingHandler.close(messageContext);
		verifyZeroInteractions(messageContext);
		verify(mockAppender, never()).doAppend(any(LoggingEvent.class));
	}

	@Test
	public void shouldReturnNull() throws Exception {
		Set<QName> headers = loggingHandler.getHeaders();
		assertTrue(headers == null);
		verify(mockAppender, never()).doAppend(any(LoggingEvent.class));
	}

	@Test
	public void shouldNotThrowExceptionWhenThereIsNoMessageInContext()
			throws FileNotFoundException {
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		loggingHandler.setDoNotThrowRuntimeExceptions();
		boolean handleMessage = loggingHandler.handleMessage(smc);

		assertTrue("handleMessage should always return true", handleMessage);
		LoggingEvent loggingEvent = getLoggingEvent();
		assertEquals("No Message to Log", loggingEvent.getMessage());
		assertEquals(Level.DEBUG, loggingEvent.getLevel());
	}

	@Test
	public void shouldCloseStream() {
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		SOAPMessage soapMessage = mock(SOAPMessage.class);

		when(smc.getMessage()).thenReturn(soapMessage);

		loggingHandler.handleMessage(smc);
		verify(mockAppender, never()).doAppend(any(LoggingEvent.class));
	}

	@Test
	public void shouldNotThrowExeptionOnInternalRuntimeException() {
		loggingHandler.setDoNotThrowRuntimeExceptions();
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		String message = "TestException";
		when(smc.getMessage()).thenThrow(new RuntimeException(message));
		loggingHandler.handleMessage(smc);
		final LoggingEvent loggingEvent = getLoggingEvent();
		assertEquals(Level.WARN, loggingEvent.getLevel());
		assertEquals("Exception in handler: ", loggingEvent.getMessage());
		assertEquals(message, loggingEvent.getThrowableInformation()
				.getThrowable().getMessage());
	}

	private LoggingEvent getLoggingEvent() {
		final ArgumentCaptor<LoggingEvent> captor = ArgumentCaptor
				.forClass(LoggingEvent.class);
		verify(mockAppender, only()).doAppend(captor.capture());
		final LoggingEvent loggingEvent = captor.getValue();
		return loggingEvent;
	}

	@Test
	public void shouldLogMessageWhenFaultDetected() {
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		loggingHandler.handleFault(smc);
		LoggingEvent loggingEvent = getLoggingEvent();
		assertEquals("No Message to Log", loggingEvent.getMessage());
		assertEquals(Level.DEBUG, loggingEvent.getLevel());
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExeptionOnInternalRuntimeException() {
		loggingHandler.setThrowRuntimeExceptions();
		SOAPMessageContext smc = mock(SOAPMessageContext.class);
		when(smc.getMessage()).thenThrow(new RuntimeException());
		loggingHandler.handleMessage(smc);
		verify(mockAppender, never()).doAppend(any(LoggingEvent.class));
	}
}
