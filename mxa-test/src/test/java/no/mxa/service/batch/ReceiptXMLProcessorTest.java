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
