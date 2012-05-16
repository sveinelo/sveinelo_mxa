package no.mxa.service.batch.confirmation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import no.mxa.UniversalConstants.LogType;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;
import no.mxa.service.NotUniqueMessageException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptXMLProcessorImplTest {
	@Mock
	private LogGenerator logGeneratorStub;
	@Mock
	private LogService logServiceStub;
	@Mock
	private MessageService messageServiceStub;

	private ReceiptXMLProcessorImpl processor;
	private HashMap<String, String> fileMap;

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
			throw new AssertionError("We need to be able to read the ResourceFiles for this test to succeed.", e);
		}
		return result;
	}

	@Before
	public void setup() throws IOException, NotUniqueMessageException {
		LogDTO logDTO = new LogDTO();
		logDTO.setLogMessage("");
		logDTO.setLogType("");
		logDTO.setMessage(new MessageDTO("1", "sendingsystem", 0, "dom", "", "", "", "", "", new Date(), "", "", "", 0, 0,
				null, null, null));

		when(logGeneratorStub.generateLog(anyString(), any(LogType.class), anyString())).thenReturn(logDTO);
		when(messageServiceStub.hasDeviation(anyString())).thenReturn(false);

		List<ReceiptAdapter> receiptAdapters = new ArrayList<ReceiptAdapter>();
		receiptAdapters.add(new CorrespondenceConfirmationAdapter());
		receiptAdapters.add(new AltUtConfirmationBatchAdapter());
		processor = new ReceiptXMLProcessorImpl(logGeneratorStub, logServiceStub, messageServiceStub, receiptAdapters);
	}

	@Test
	public void parse_empty_xml() {
		processor.process("", "");
	}

	@Test
	public void parseOldRecieptXML() throws IOException {
		boolean success = processor.process(fileMap.get("AltUtConfirmationBatchExample.xml"), "file.log");
		assertTrue(success);
	}

	@Test
	public void parseNewRecieptXML() throws IOException {
		boolean success = processor.process(fileMap.get("CorrespondenceConfirmationsEksempel.xml"), "file.log");
		assertTrue(success);
	}

}
