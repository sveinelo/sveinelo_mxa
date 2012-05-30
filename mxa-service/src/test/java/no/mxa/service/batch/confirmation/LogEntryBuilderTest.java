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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import no.mxa.service.batch.confirmation.LogEntryBuilder.IllegalLogEntryStateException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class LogEntryBuilderTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogEntryBuilderTest.class);

	@Test
	public void testBuildRead() throws InvalidLogEntryTypeException {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.setConfirmedDateTime("dateTime");
		builder.setConfirmedRoleList("roleList");
		builder.setLoginMethod("3");
		builder.setMessageReference("ABC");
		builder.setParticipantId("lkjj");
		builder.setReadDateTime("Today");
		builder.setShortName("shorty");
		LogEntry logEntry = builder.build();
		assertNotNull(logEntry);
		LOGGER.debug(logEntry.toString());
		assertEquals("reportee: null; readDateTime: Today; shortName: shorty;", logEntry.toLogEntryMessage());
	}

	@Test
	public void testBuildConfirmed() throws InvalidLogEntryTypeException {
		LogEntryBuilder builder = new LogEntryBuilder("Confirmed");
		builder.setConfirmedDateTime("dateTime");
		builder.setConfirmedRoleList("roleList");
		builder.setLoginMethod("3");
		builder.setMessageReference("ABC");
		builder.setParticipantId("lkjj");
		builder.setReadDateTime("Today");
		builder.setShortName("shorty");
		LogEntry logEntry = builder.build();
		assertNotNull(logEntry);
		LOGGER.debug(logEntry.toString());
		assertEquals(
				"reportee: null; participantId: lkjj; shortName: shorty; confirmedDateTime: dateTime; confirmedRoleList roleList; loginMethod: 3;",
				logEntry.toLogEntryMessage());
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible1() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setConfirmedDateTime("lkhg");
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible2() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setConfirmedRoleList("lkhg");
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible3() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setLoginMethod("lkhg");
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible4() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setMessageReference("lkhg");
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible5() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setParticipantId("lkhg");
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible6() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setReadDateTime("lkhg");
	}

	@Test(expected = IllegalLogEntryStateException.class)
	public void testReuseImpossible7() {
		LogEntryBuilder builder = new LogEntryBuilder("Read");
		builder.build();
		builder.setShortName("lkhg");
	}

	@Test(expected = InvalidLogEntryTypeException.class)
	public void testEmptyTypeFails() throws InvalidLogEntryTypeException {
		LogEntryBuilder builder = new LogEntryBuilder(null);
		LogEntry entry = builder.build();
		entry.toLogEntryMessage();
	}

	@Test(expected = InvalidLogEntryTypeException.class)
	public void testWrongTypeFails() throws InvalidLogEntryTypeException {
		LogEntryBuilder builder = new LogEntryBuilder("kjjhgdsf");
		LogEntry entry = builder.build();
		entry.toLogEntryMessage();
	}
}
