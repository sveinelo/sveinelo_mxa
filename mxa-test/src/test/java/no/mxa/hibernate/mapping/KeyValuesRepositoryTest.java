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

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dataaccess.KeyValuesRepository;
import no.mxa.dto.KeyValuesDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The tests in this class were used during Hibernate configuration. Key values are now retrieved through initial application
 * post construct
 */
@Transactional
public class KeyValuesRepositoryTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	private KeyValuesRepository repository;
	private Long existingId = 2L;

	@Inject
	public void setRepository(KeyValuesRepository repository) {
		this.repository = repository;
	}

	/**
	 * Initial setup. Requires data in keyvalues table
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, existingId, "GOVORGAN", null, null, "Test", "Description");
	}

	/**
	 * Test findById. Requires data in keyvalues table
	 */
	@Test
	public void testFindById() {
		KeyValuesDTO result = repository.findById(existingId);
		assertThat(result, is(notNullValue()));
		assertThat(result.getStringValue(), is("Test"));
	}

	/**
	 * Test save
	 */
	@Test
	public void testSave() {
		KeyValuesDTO instance = repository.findById(existingId);
		instance.setNumericValue(12);
		repository.save(instance);

		KeyValuesDTO updatedInstance = repository.findById(existingId);
		assertThat(updatedInstance, is(notNullValue()));
		assertThat(updatedInstance.getNumericValue(), is(12));
	}

}
