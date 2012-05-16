package no.mxa.hibernate.mapping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dataaccess.ContactInfoRepository;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The tests in this class were used during Hibernate configuration. Contact infos are now retrieved through parent message
 * objects
 */
@Transactional
public class ContactInfoRepositoryTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	private ContactInfoRepository repository;
	private MessageDTO message;
	private Long existingId = 1L;

	@Inject
	public void setRepository(ContactInfoRepository repository) {
		this.repository = repository;
	}

	/**
	 * Initial setup. Requires data in contactinfo table
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

		String contactInfoQuery = "INSERT INTO CONTACTINFO (ID, MESSAGEID, TYPE, ADDRESS) VALUES (?, ?, ?, ?)";
		template.update(contactInfoQuery, existingId, 1, "SMS", "99999999");
	}

	/**
	 * Test findById. Requires data in contactinfo table
	 */
	@Test
	public void testFindById() {
		ContactInfoDTO result = repository.findById(existingId);
		assertThat(result, is(notNullValue()));
		assertThat(result.getType(), is("SMS"));
	}

	/**
	 * Test save
	 */
	@Test
	public void testSave() {
		ContactInfoDTO instance = repository.findById(existingId);
		instance.setAddress("Ny adresse");
		repository.save(instance);

		ContactInfoDTO updatedInstance = repository.findById(existingId);
		assertThat(updatedInstance.getAddress(), is("Ny adresse"));
	}

	/**
	 * Test findByProperty. Requires data in contactinfo table
	 */
	@Test
	public void testFindByProperty() {
		ContactInfoDTO result = repository.findById(existingId);
		message = result.getMessage();

		List<ContactInfoDTO> results = repository.findByProperty("message", message);
		assertThat(results.get(0), is(notNullValue()));
	}

}