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
package no.mxa.ws.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class EnhancedParserTest {

	private Parser parser; // Object to be tested.

	private static String xml_unpredictable_linebreaks;
	private static String xml_flat;
	private static String xml_eksempel;
	private static String xml_linebreak_in_EmailAddress;
	private static String xml_264;
	/** file which failed on EmailAddress, flushed from test-environment on 15. okt. 2009 */
	private static String xml_460;

	@BeforeClass
	public static void setUp() throws Exception {
		final String folder = "agency_to_mxa/";
		xml_unpredictable_linebreaks = getXml(folder + "message_SANT_to_MXA_with_unpredictable_linebreaks.xml");
		xml_flat = getXml(folder + "message_SANT_to_MXA_flat.xml");
		xml_eksempel = getXml(folder + "message_SANT_to_MXA_eksempel.xml");
		xml_linebreak_in_EmailAddress = getXml(folder + "message_SANT_to_MXA_with_linebreak_in_EmailAddress.xml");
		xml_264 = getXml(folder + "264.xml");
		xml_460 = getXml(folder + "1255598292005.xml");
	}

	@Before
	public void setup() {
		parser = new Parser(new ClassPathResource("no/mxa/ws/xml/wsdl/SantMxa.xsd"));
	}

	@Test
	public void validate_460() {
		String expected = "OK";
		String result = parser.validateDocument(xml_460);
		assertEquals(expected, result);
	}

	@Test
	public void parsing_460_file_from_test_environment() {
		Message message = parser.parseDocument(xml_460);
		List<ContactInfo> contactInfoList = message.getContactInfo();
		assertNotNull(contactInfoList);
		assertTrue("There were no contactInfo", contactInfoList.size() > 0);
		assertTrue("There were more or less than one contact", contactInfoList.size() == 1);

		for (ContactInfo contactInfo : contactInfoList) {
			assertNotNull(contactInfo);
			assertEquals("EmailAddress does not match.", "mail@oslopatent.no", contactInfo.getAddress());
		}
	}

	@Test
	public void validate_264() {
		String expected = "OK";
		String result = parser.validateDocument(xml_264);
		assertEquals(expected, result);
	}

	@Test
	public void validate_unpredictable_linebreaks() {
		String expected = "OK";
		String result = parser.validateDocument(xml_unpredictable_linebreaks);
		assertEquals(expected, result);
	}

	@Test
	public void validate_flat() {
		String expected = "OK";
		String result = parser.validateDocument(xml_flat);
		assertEquals(expected, result);
	}

	@Test
	public void validate_linebreak_in_EmailAddress() {
		// This one is expected to return an error result.
		String expected1 = "Error: cvc-type.3.1.3: The value 'oslo@\n        patentkontor.com' of element 'EmailAddress' is not valid.";
		String result1 = parser.validateDocument(xml_linebreak_in_EmailAddress);
		assertEquals(expected1, result1);

		String expected2 = "OK";
		String result2 = parser.validateDocument(xml_eksempel);
		assertEquals("Maybe previous parse Error are not reset.", expected2, result2);
	}

	@Test
	public void test_parsing_of_message_SANT_to_MXA_flat() throws IOException, SQLException {
		Message message = parser.parseDocument(xml_flat);
		assertEquals("AM12345", message.getAltinnArchive());
		assertEquals(0, message.getBatchSending());
		assertEquals("test sans player", message.getCaseDescription());
		assertEquals("mcr,Charrier Mathieu", message.getCaseOfficer());
		assertEquals("VM&amp;nbsp;200900061,Main test altut (pdf),Deres ref&amp;nbsp;E29723/soi/test",
				message.getContentMessageHeader());
		assertEquals(
				"Må besvares innen:&lt;br /&gt;Saksnummer:VM&amp;nbsp;200900061&amp;nbsp;&lt;br /&gt;Brevtype:Main test altut (pdf)&lt;br /&gt;Deres ref:E29723/soi/test&lt;br /&gt;Status:01000&amp;nbsp;Under behandling&amp;nbsp;Mottatt&lt;br /&gt;Tittel:test sans player&lt;br /&gt;Patentstyrets saksbehandler:mcr",
				message.getContentMessageSummary());
		assertEquals("VM", message.getDomain());
		assertEquals(null, message.getDueDate());
		assertEquals("1111133", message.getIdproc());
		assertEquals("VM20090006136-011111133", message.getMessageKey());
		assertEquals("VM20090006136-01", message.getMessageReference());
		assertEquals("31124901793", message.getParticipantId());
		assertEquals("AGENCY", message.getSendingSystem());

		List<Attachment> attachmentList = message.getAttachments();
		assertNotNull(attachmentList);
		assertTrue("Number of Attachements were different from one.", attachmentList.size() == 1);
		for (Attachment attachment : attachmentList) {

			assertNotNull(attachment.getBase64EncodedString());

			String base64EncodedString = attachment.getBase64EncodedString();

			assertEquals("attachement content should match input", "TVhBLWRva3VtZW50IHRpbCBBbHR1dC4K", base64EncodedString);

			assertEquals("filename.txt", attachment.getFilename());
			assertEquals("text/plain", attachment.getMimeType());
			assertEquals("Svar på patentsøknad", attachment.getName());
		}

		List<ContactInfo> contactInfoList = message.getContactInfo();
		assertNotNull(contactInfoList);
		assertTrue("Number of ContactInfos were different from one.", contactInfoList.size() == 2);
		assertEquals("SMS", contactInfoList.get(0).getType());
		assertEquals("+332145864", contactInfoList.get(0).getAddress());
		assertEquals("EMAIL", contactInfoList.get(1).getType());
		assertEquals("oslo@patentkontor.com", contactInfoList.get(1).getAddress());
	}

	@Test
	public void parsing_three_sucsessive_messages() {
		Message message = parser.parseDocument(xml_unpredictable_linebreaks);
		parser.parseDocument(xml_eksempel);
		parser.parseDocument(xml_flat);
		Iterator<Attachment> it = message.getAttachments().iterator();
		while (it.hasNext()) {
			it.next();
		}
	}

	@Test
	public void parsing_example_file_from_jens_petter_264() {
		Message message = parser.parseDocument(xml_264);
		List<ContactInfo> contactInfoList = message.getContactInfo();
		assertNotNull(contactInfoList);
		assertTrue("There were no contactInfo", contactInfoList.size() > 0);
		assertTrue("There were more or less than one contact", contactInfoList.size() == 1);

		for (ContactInfo contactInfo : contactInfoList) {
			assertNotNull(contactInfo);
			assertEquals("EmailAddress does not match.", "mail@oslopatent.no", contactInfo.getAddress());
		}
	}

	/**
	 * @param filename
	 * @return String representation of the file where each line end in <code>\n</code>
	 * @throws Exception
	 */
	private static String getXml(String filename) throws Exception {
		ClassPathResource classPathResource = new ClassPathResource(filename);
		InputStream is = classPathResource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
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
