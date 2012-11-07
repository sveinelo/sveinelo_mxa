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
package no.mxa.service.implementations;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.UniversalConstants;
import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class LogGeneratorTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	@Inject
	private LogGenerator logGenerator;
	private LogDTO logDTO;
	@Inject
	private MessageRepository messageRepository;
	private Long existingId = 58L;

	@Before
	public void setUp() throws Exception {
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, existingId, "Key001", "AGENCY", 0, "PS", "01010101010", "MSREF", "Proc", "Header",
				"Summary", 1, 10, new Date(), 0);
	}

	@Test
	public void testGenerateLogStandalone() {
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.RCT_FTP_FAULT);

		assertThat(logDTO.getLogMessage(), is(notNullValue()));
	}

	@Test
	public void testGenerateLogWithMessageId() {
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.MSG_SENT_ALTINN, existingId);

		assertThat(logDTO.getMessage().getId(), is(equalTo(existingId)));
	}

	@Test
	public void testGenerateLogWithMessageIdAndTimestamp() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.MSG_SENT_ALTINN, existingId, now);

		assertThat(logDTO.getTime(), is(now));
	}

	@Test
	public void testGenerateLogWithMessageReference() {
		logDTO = logGenerator.generateLog(" <Ekstra logg>", UniversalConstants.MSG_SENT_ALTINN, existingId);
		MessageDTO message = messageRepository.findById(existingId);

		assertThat(logDTO.getMessage(), is(message));
	}

	@Test
	public void testSaveLog() {
		logDTO = logGenerator.generateLog(" Ekstra logg", "Oversendelse Altinn", existingId);
		logGenerator.saveLog(logDTO);
		Long savedId = logDTO.getId();

		assertThat(savedId, is(notNullValue()));
	}

}
