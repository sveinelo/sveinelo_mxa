/*
 * #%L
 * Altinn Webservice
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
