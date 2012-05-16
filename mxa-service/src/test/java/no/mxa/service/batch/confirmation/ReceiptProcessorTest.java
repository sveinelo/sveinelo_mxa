package no.mxa.service.batch.confirmation;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import no.mxa.service.KeyValues;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptProcessorTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptProcessorTest.class);
	@Mock
	private LogService logService;
	@Mock
	private LogGenerator logGenerator;
	@Mock
	private KeyValues keyValues;
	@Mock
	private ReceiptXMLProcessor receiptXMLProcessor;

	private ReceiptProcessor receiptProcessor;
	private Map<String, String> fileMap;

	@Before
	public void setUp() {
		receiptProcessor = new ReceiptProcessor(keyValues, receiptXMLProcessor, logGenerator, logService);
	}

	@Before
	public void initTestFiles() {
		fileMap = new HashMap<String, String>();
		addFileToMap("BOMTest.log");
		addFileToMap("AltUtConfirmationBatchExample.xml");
		addFileToMap("AltUtConfirmationBatchExampleBOM.xml");
		addFileToMap("BrokenAltUtConfirmationBatchExample.xml");
		addFileToMap("CorrespondenceConfirmationsEksempel.xml");
	}

	private void addFileToMap(String filename) {
		fileMap.put(filename, convertResource2String(new ClassPathResource(filename)));
	}

	private String convertResource2String(Resource resource) {
		String result;
		try (StringWriter writer = new StringWriter();) {
			IOUtils.copy(resource.getInputStream(), writer);
			result = writer.toString();
		} catch (IOException e) {
			LOGGER.error("Error reading from File.", e);
			throw new AssertionError("We need to be able to read the ResourceFiles for this test to succeed.", e);
		}
		return result;
	}

	@Test
	public void testStringProcessing() {
		receiptProcessor.processXML(fileMap);
		verify(receiptXMLProcessor, times(5)).process(anyString(), anyString());
	}

}
