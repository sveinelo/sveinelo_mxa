/*
 * #%L
 * Integration tests
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
package no.mxa.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dto.LogDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class LogServiceTest extends SpringBasedTest {

	@Inject
	LogService logService;
	@Inject
	DataSource dataSource;

	@Before
	public void setUp() {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String withoutReferenceQuery = "INSERT INTO LOG (ID, TIME, LOGTYPE, LOGMSG) VALUES (?, ?, ?, ?)";
		template.update(withoutReferenceQuery, 1, new Date(), "No ref", "No message reference");

		String withReferenceQuery = "INSERT INTO LOG (ID, MESSAGEID, TIME, LOGTYPE, LOGMSG) VALUES (?, ?, ?, ?, ?)";
		template.update(withReferenceQuery, 2, 1, new Date(), "Ref", "Message reference");
	}

	@Test
	public void shouldReturnLogEntryWithoutMessageReference() {
		List<LogDTO> logList = logService.getAllLogsWithNullInMessageId();
		assertThat(logList.size(), is(1));
		assertThat(logList.get(0).getLogType(), is("No ref"));
	}

}
