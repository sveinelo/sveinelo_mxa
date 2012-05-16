package no.mxa.ws.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ParserTest extends SpringBasedTest {

	@Inject
	private DTOGenerator dtoGenerator;
	@Inject
	private Parser parser;
	private String xmlString;
	private MessageDTO messageDTO;

	@Before
	public void setUp() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("agency_to_mxa/SANTMXA_eksempel.xml");
		InputStream is = classPathResource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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
		xmlString = sb.toString();
	}

	@Test
	public void testValidate() {
		String validationResult = parser.validateDocument(xmlString);
		assertEquals(validationResult, "OK");
	}

	@Test
	public void testParse() {
		Message message = parser.parseDocument(xmlString);

		messageDTO = dtoGenerator.generateMessageDTO(message);

		assertNotNull(messageDTO.getAltinnArchive());
		assertNotNull(message.getIdproc());
	}

}
