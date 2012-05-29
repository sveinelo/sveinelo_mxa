package no.mxa.ws.webservice;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import no.mxa.UniversalConstants;
import no.mxa.service.LogGenerator;
import no.mxa.service.MessageService;
import no.mxa.utils.UnicodeUtil;
import no.mxa.ws.parser.DTOGenerator;
import no.mxa.ws.parser.Message;
import no.mxa.ws.parser.Parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MXATest {
	@Mock
	private DTOGenerator dtoGenerator;

	@Mock
	private LogGenerator logGenerator;

	@Mock
	private MessageService messageService;

	@Mock
	private Parser parser;
	private MXA mxa;

	private String testXML;

	@Before
	public void createMXA() throws Exception {
		mxa = new MXA(parser, dtoGenerator, messageService, logGenerator);
		mxa.setThrowExceptions(true);
	}

	@Before
	public void readExampleXml() throws IOException {
		// Only the first chars are interesting for these tests.
		testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void verifyFileCanBeProcessed() throws IOException {
		when(parser.validateDocument(anyString())).thenReturn("OK");
		Message value = new Message();
		when(parser.parseDocument(anyString())).thenReturn(value);
		int returnCode = mxa.submitMessage(testXML);
		assertEquals("This piece should give a returnCode of 0 indicating that all is well.",
				UniversalConstants.WEB_SERVICE_MESSAGE_SAVED, returnCode);
	}

	@Test
	public void checkFalseDocument() throws IOException {
		when(parser.validateDocument(anyString())).thenReturn("NOT OK");
		Message value = new Message();
		when(parser.parseDocument(anyString())).thenReturn(value);
		int returnCode = mxa.submitMessage(testXML);
		assertEquals("This piece should give a returnCode of 2 indicating that all is well.",
				UniversalConstants.WEB_SERVICE_MESSAGE_VALIDATION_ERROR, returnCode);
	}

	@Test
	public void verifyBomFileCanBeProcessed() throws IOException {
		String bomXML = UnicodeUtil.convert(testXML, "UTF-8");
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		when(parser.validateDocument(captor.capture())).thenReturn("OK");
		Message value = new Message();
		when(parser.parseDocument(anyString())).thenReturn(value);
		mxa.submitMessage(bomXML);
		assertThat(captor.getValue(), is(equalTo(testXML)));
		assertEquals("The BOM part should be removed from the Message before being sendt to the parser.", testXML,
				captor.getValue());
	}

}
