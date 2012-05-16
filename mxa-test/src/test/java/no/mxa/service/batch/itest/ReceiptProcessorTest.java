package no.mxa.service.batch.itest;

import java.io.IOException;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.service.NotUniqueMessageException;
import no.mxa.service.batch.confirmation.ReceiptProcessor;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReceiptProcessorTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	@Inject
	private ReceiptProcessor receiptProcessor;

	/**
	 * Initial setup.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, 1L, "RECEIPTFTPSERVER", null, null, "0.0.0.000", null);
		template.update(keyvaluesQuery, 2L, "RECEIPTFTPUSER", null, null, "ftp_user", null);
		template.update(keyvaluesQuery, 3L, "RECEIPTFTPPASSWORD", null, null, "passord", null);
		template.update(keyvaluesQuery, 4L, "RECEIPTFTPPATH", null, null, "/altinn/test/kvittering", null);
	}

	@Test
	public void testReceiptProcessor() throws IOException, NotUniqueMessageException {
		receiptProcessor.process();
	}

}
