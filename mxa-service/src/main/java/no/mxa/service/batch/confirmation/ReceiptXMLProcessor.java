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

public interface ReceiptXMLProcessor {

	/**
	 * Parses the xml and updates message with new status based on the confirmation xml.
	 * 
	 * @param xml
	 *            string representation of the xml to process
	 * @param filename
	 *            original filename of the file the xml comes from
	 * @return true if process succeeds, false otherwise
	 */
	boolean process(String xml, String filename);

}
