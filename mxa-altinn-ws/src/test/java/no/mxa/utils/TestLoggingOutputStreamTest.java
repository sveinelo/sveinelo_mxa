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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import no.mxa.utils.LoggingOutputStream.Level;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class TestLoggingOutputStreamTest {

	@Mock
	private Logger logger;

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongInit1() {
		new LoggingOutputStream(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongInit2() {
		new LoggingOutputStream(logger, null);
	}

	@Test
	public void testLogToError() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger,
				Level.ERROR); PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		verify(logger, only()).error(anyString());
	}

	@Test
	public void testLogToWarn() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger,
				Level.WARN); PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		verify(logger, only()).warn(anyString());
	}

	@Test
	public void testLogToInfo() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger,
				Level.INFO); PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		verify(logger, only()).info(anyString());
	}

	@Test
	public void testLogToDebug() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger,
				Level.DEBUG); PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		verify(logger, only()).debug(anyString());
	}

	@Test
	public void testLogToTrace() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger,
				Level.TRACE); PrintStream printStream = new PrintStream(out);) {
			printStream.println("This is a test");
		}
		verify(logger, only()).trace(anyString());
	}

	@Test
	public void testBigLog() {
		try (LoggingOutputStream out = new LoggingOutputStream(logger,
				Level.TRACE); PrintStream printStream = new PrintStream(out);) {
			for (int i = 0; i < 200; i++)
				printStream.println("This is a test");
		}
		verify(logger, only()).trace(anyString());
	}

}
