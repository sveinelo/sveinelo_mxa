/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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
package no.mxa.service.altut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.mxa.altinn.castor.xsd.AltUtConfirmationBatch;
import no.mxa.altinn.castor.xsd.Confirmation;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AltUtConfirmationValidator {

	/**
	 * List of files we know the "Read" tag is missing. For this to be usefull, set minOccurs="0" in the AltUt XSD file.
	 */
	private final String[] failedFiles = new String[] { "20120305T040013_PAT_PAT_CONF.log", "altut-2011-11-29T030020.log",
			"altut-2011-12-17T030028.log", "altut-2012-01-13T030024.log", "altut-2012-01-20T030042.log",
			"altut-2012-01-21T030035.log", "altut-2012-01-25T030042.log", "altut-2012-02-01T030038.log",
			"altut-2012-02-14T030023.log", "altut-2012-03-16T030011.log", "altut-2012-06-19T020014.log", "PAT34717.xml" };

	private static final Logger log = LoggerFactory.getLogger(AltUtConfirmationValidator.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length > 0) {
			new AltUtConfirmationValidator().validate(args[0]);
		}

	}

	@Test
	@Ignore
	public void testLocalFiles() throws IOException {
		// Set this location to wherever the files to be checked are.
		validate("C:\\Users\\abichper\\Documents\\MXA\\kvitteringer\\prod\\");
	}

	/**
	 * Determines if the name provided is a directory or file and then calls
	 * {@link AltUtConfirmationValidator#validateFile(File, List)} on the file or the files in the directory.
	 * 
	 * @param pathname
	 * @throws IOException
	 */
	public void validate(String pathname) throws IOException {
		File processingDir = new File(pathname);
		List<String> failedFileNames = new ArrayList<>();
		if (processingDir.isDirectory()) {
			for (File file : processingDir.listFiles()) {
				validateFile(file, failedFileNames);
			}
		} else {
			validateFile(processingDir, failedFileNames);
		}

		for (String failedFileName : failedFileNames) {
			log.error(failedFileName);
		}
	}

	/**
	 * Validates a single file and puts the filename in the provided list if the file fails.
	 * 
	 * @param file
	 * @param failedFileNames
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void validateFile(File file, List<String> failedFileNames) throws FileNotFoundException, IOException {
		// Open file as InputStream, strip the BOM away and make a reader
		try (FileInputStream delegate = new FileInputStream(file);
				BOMInputStream bis = new BOMInputStream(delegate, ByteOrderMark.UTF_8);
				Reader reader = new InputStreamReader(bis)) {
			try {
				// Try to unmashall the XML
				AltUtConfirmationBatch batch = (AltUtConfirmationBatch) Unmarshaller.unmarshal(AltUtConfirmationBatch.class,
						reader);
				// if the file is in the list of failed files, try to find the messageReference for all confirmations that lack
				// the Read tag.
				if (Arrays.asList(failedFiles).contains(file.getName())) {
					for (Confirmation confirmation : batch.getAltUtConfirmations().getConfirmation()) {
						if (confirmation.getRead() == null) {
							log.error("Missing 'Read' tag: Filename='{}'; MessageReference='{}'", file.getName(),
									confirmation.getMessageReference());
						}
					}
				}
			} catch (MarshalException e) {
				log.error("Problem unmashalling file '{}': {}", file.getName(), e.getMessage());
				failedFileNames.add(file.getName());
			} catch (ValidationException e) {
				log.error("Problem validating file '{}': {}", file.getName(), e.getMessage());
				failedFileNames.add(file.getName());
			}
		}

	}
}
