/*
 * #%L
 * Integration tests
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
package no.mxa.hibernate.mapping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dataaccess.LogRepository;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The tests in this class were used during Hibernate configuration. Logs are now retrieved through parent message objects
 */
@Transactional
public class LogRepositoryTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	private LogRepository repository;
	private MessageDTO message;
	private Long existingId = 20L;

	@Inject
	public void setRepository(LogRepository repository) {
		this.repository = repository;
	}

	/**
	 * Initial setup. Requires data in log table
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);

		String logQuery = "INSERT INTO LOG (ID, MESSAGEID, TIME, LOGTYPE, LOGMSG) VALUES (?, ?, ?, ?, ?)";
		template.update(logQuery, existingId, 1, new Date(), "File log", "Message saved");
	}

	/**
	 * Test findById. Requires data in log table
	 */
	@Test
	public void testFindById() {
		LogDTO result = repository.findById(existingId);
		assertThat(result, is(notNullValue()));
		assertThat(result.getLogMessage(), is("Message saved"));
	}

	/**
	 * Test save
	 */
	@Test
	public void testSave() {
		LogDTO instance = repository.findById(existingId);
		assertThat(instance, is(notNullValue()));
		instance.setLogMessage("Updated");
		repository.save(instance);

		LogDTO updatedInstance = repository.findById(existingId);
		assertThat(updatedInstance.getLogMessage(), is("Updated"));
	}

	/**
	 * Test find parent. Requires data in log table
	 */
	@Test
	public void testFindByPropertyMessage() {
		LogDTO result = repository.findById(existingId);
		message = result.getMessage();

		List<LogDTO> results = repository.findByProperty("message", message);
		assertThat(results.get(0), is(notNullValue()));
	}

}
