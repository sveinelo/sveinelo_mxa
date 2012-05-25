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
package no.mxa.hibernate.mapping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dataaccess.AttachmentRepository;
import no.mxa.dto.AttachmentDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The tests in this class were used during Hibernate configuration. Attachments are now retrieved through parent message
 * objects
 */
@Transactional
public class AttachmentRepositoryTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	private AttachmentRepository repository;
	private MessageDTO message;
	private AttachmentDTO result;
	private Long existingId = 1L;

	@Inject
	public void setRepository(AttachmentRepository repository) {
		this.repository = repository;
	}

	/**
	 * Initial setup. Requires data in attachment table
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

		String attachmentQuery = "INSERT INTO ATTACHMENT (ID, MESSAGEID, ATTACHMENT, MIMETYPE, FILENAME, NAME) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(attachmentQuery, existingId, 1, "Attachment", "Mimetype", "Filename", "Name");
	}

	/**
	 * Test findById. Requires data in attachment table
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testFindById() throws SQLException {
		result = repository.findById(existingId);

		assertThat(result, is(notNullValue()));
		assertThat(result.getMimeType(), is("Mimetype"));
	}

	/**
	 * Test save. Requires data in attachment table
	 */
	@Test
	public void testSave() {
		AttachmentDTO result = repository.findById(existingId);
		assertThat(result, is(notNullValue()));
		result.setFileName("Nytt filnavn");
		repository.save(result);

		AttachmentDTO updatedResult = repository.findById(existingId);
		assertThat(updatedResult, is(notNullValue()));
		assertThat(updatedResult.getFileName(), is("Nytt filnavn"));
	}

	/**
	 * Test findByProperty. Requires data in attachment table
	 */
	@Test
	public void testFindByProperty() {
		AttachmentDTO result = repository.findById(existingId);
		message = result.getMessage();
		List<AttachmentDTO> results = repository.findByProperty("message", message);

		assertThat(results.get(0), is(notNullValue()));
	}

	/**
	 * Test findClob. Requires data in attachment table
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testFindClob() throws SQLException {
		result = repository.findById(existingId);

		assertThat(result.getBase64EncodedAttachement(), is(notNullValue()));
	}

}
