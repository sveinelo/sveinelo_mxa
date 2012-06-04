/*
 * #%L
 * Service
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
package no.mxa.service.batch.confirmation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.io.input.BOMInputStream;
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
		addFileToMap("emptyFileAltut.xml");
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
			InputStream inputStream = new BOMInputStream(resource.getInputStream());
			IOUtils.copy(inputStream, writer);
			result = writer.toString();
			result = new String(result.getBytes(), "UTF-8");
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
		boolean success = processor.process(fileMap.get("AltUtConfirmationBatchExample.xml"), "someFile.log");
		assertTrue("Old files should process fine.", success);
	}

	@Test
	public void parseEmptyrOldRecieptXML() throws IOException {
		boolean success = processor.process(fileMap.get("emptyFileAltut.xml"), "file.log");
		assertTrue(success);
	}

	@Test
	public void parseNewRecieptXML() throws IOException {
		boolean success = processor.process(fileMap.get("CorrespondenceConfirmationsEksempel.xml"), "file.log");
		assertTrue(success);
	}

}
