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