package no.mxa.altinn.ws.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import no.mxa.UniversalConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilePrintStreamFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilePrintStreamFactory.class);

	/**
	 * Defaults to System.out if file can not be created.
	 * 
	 * @param file
	 * @return
	 */
	PrintStream create() {
		String tmpDir = System.getProperty("java.io.tmp");
		PrintStream out;
		try {
			out = new PrintStream(new File(System.nanoTime() + ".xml"), UniversalConstants.CHARACTER_SET_UTF8);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			LOGGER.debug("Defaulting to System.out", e);
			out = System.out;
		}
		return out;
	}

}
