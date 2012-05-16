package no.mxa.ws.webservice;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SubmitMessageTest extends SpringBasedTest {

	@Inject
	private MXA mxa;
	private String xmlString;

	@Before
	public void setUp() throws IOException {
		ClassPathResource classPathResource = new ClassPathResource("mxa_to_altinn/MessageExample.xml");
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
		is.close();
		reader.close();

		xmlString = sb.toString();

	}

	@Test
	public void testSubmitMessage() {
		int returnCode = mxa.submitMessage(xmlString);
		assertEquals(0, returnCode);
	}
}
