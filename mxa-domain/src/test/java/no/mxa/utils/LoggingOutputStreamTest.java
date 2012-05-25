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
package no.mxa.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import no.mxa.utils.LoggingOutputStream.Level;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class LoggingOutputStreamTest {

	@Mock
	private Logger logger;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void shouldSuppressBlankLines() {
		try (LoggingOutputStream los = new LoggingOutputStream(logger, Level.INFO); PrintStream out = new PrintStream(los);) {
			out.println("Test");
			out.flush();
			out.println();
			out.flush();
			out.print("\r");
			out.flush();
			out.print("\n");
			out.flush();
			out.print("\rTTT");
			out.flush();
		}
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, times(2)).info(captor.capture());
		List<String> expected = new ArrayList<>();
		expected.add("Test" + System.lineSeparator());
		expected.add("\rTTT");
		assertEquals(expected, captor.getAllValues());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongInit1() {
		new LoggingOutputStream(null, null).close();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongInit2() {
		try (LoggingOutputStream loggingOutputStream = new LoggingOutputStream(logger, null);) {
			// Nothing to do here
		}
	}

	@Test(expected = IOException.class)
	public void shouldFailOnClosedStream() throws IOException {
		LoggingOutputStream loggingOutputStream = new LoggingOutputStream(logger, Level.ERROR);
		loggingOutputStream.close();
		loggingOutputStream.write("Test".getBytes());
	}

	@Test
	public void testLogToError() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger, Level.ERROR);
				PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, only()).error(captor.capture());
		assertEquals("This is a test" + System.lineSeparator(), captor.getValue());
	}

	@Test
	public void testLogToWarn() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger, Level.WARN);
				PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, only()).warn(captor.capture());
		assertEquals("This is a test" + System.lineSeparator(), captor.getValue());
	}

	@Test
	public void testLogToInfo() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger, Level.INFO);
				PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, only()).info(captor.capture());
		assertEquals("This is a test" + System.lineSeparator(), captor.getValue());
	}

	@Test
	public void testLogToDebug() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger, Level.DEBUG);
				PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, only()).debug(captor.capture());
		assertEquals("This is a test" + System.lineSeparator(), captor.getValue());
	}

	@Test
	public void testLogToTrace() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger, Level.TRACE);
				PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, only()).trace(captor.capture());
		assertEquals("This is a test" + System.lineSeparator(), captor.getValue());
	}

	@Test
	public void testBigLog() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger, Level.TRACE);
				PrintStream printStream = new PrintStream(out);) {
			for (int i = 0; i < 200; i++) {
				printStream.println("This is a test");
			}
		}
		verify(logger, only()).trace(anyString());
	}

}
