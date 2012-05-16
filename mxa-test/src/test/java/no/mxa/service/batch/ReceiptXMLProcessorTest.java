package no.mxa.service.batch;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import no.mxa.service.batch.confirmation.ReceiptXMLProcessorImpl;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.xml.sax.SAXException;

public class ReceiptXMLProcessorTest extends SpringBasedTest {
	private static final String folder = "altinn_to_mxa_confirmation/";

	@Inject
	private DataSource dataSource;
	@Inject
	private ReceiptXMLProcessorImpl receiptXMLProcessor;

	/**
	 * Initial setup.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String messageQuery = "INSERT INTO MESSAGE (ID, MESSAGEKEY, SENDINGSYSTEM, BATCHSENDING, DOMAIN, "
				+ "PARTICIPANTID, MESSAGEREFERENCE, IDPROC, MESSAGEHEADER, MESSAGESUMMARY, SENTALTINN, "
				+ "MSG_STATUS, READDEADLINE, OVERDUENOTICESENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?)";
		template.update(messageQuery, 2L, "Key001", "AGENCY", 0, "PS", "01010101010", "P1234-12", "Proc", "Header", "Summary",
				1, 10, new Date(), 0);
	}

	@Test
	public void testReceiptXMLProcessor() throws SAXException, IOException, ParserConfigurationException {
		String xml = convertStreamToString((new ClassPathResource(folder + "AltUtConfirmationBatchExample.xml"))
				.getInputStream());
		boolean success = receiptXMLProcessor.process(xml, "AltUtConfirmationBatchExample.xml");
		assertTrue(success);
	}

	@Test
	public void testReceiptXMLProcessorBrokenXml() throws SAXException, IOException, ParserConfigurationException {
		String xml = convertStreamToString((new ClassPathResource(folder + "BrokenAltUtConfirmationBatchExample.xml"))
				.getInputStream());
		boolean success = receiptXMLProcessor.process(xml, "BrokenAltUtConfirmationBatchExample.xml");
		assertFalse(success);
	}

	@Test
	public void shouldParseNewCorrespondenceConfirmationFormat2010() throws SAXException, IOException,
			ParserConfigurationException {
		String xml = convertStreamToString((new ClassPathResource(folder
				+ "correspondence_confirmations_2010_10_example_generated_by_eclipse.xml")).getInputStream());
		boolean success = receiptXMLProcessor.process(xml,
				"correspondence_confirmations_2010_10_example_generated_by_eclipse.xml");
		assertTrue(success);
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}
