/*
 * #%L
 * Web Archive
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
package no.mxa.web.converter;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class DateWithHoursAndSecondsConverterTest {

	private static final long DATE_LONG_VALUE = 1336397812271L;
	private static final String DATE_STRING_VALUE = "2012.05.07 15:36";
	private DateWithHoursAndSecondsConverter conv;
	private Date date;

	@Before
	public void setUp() {
		conv = new DateWithHoursAndSecondsConverter();
		date = new Date(DATE_LONG_VALUE);
	}

	@Test(expected = NotImplementedException.class)
	public void testGetAsObject() {
		conv.getAsObject(null, null, null);
	}

	@Test
	public void testGetAsStringValidParams() {
		assertEquals("Converter running with null values but real date should return the right date.", DATE_STRING_VALUE,
				conv.getAsString(null, null, date));
	}

	@Test
	public void testGetAsStringNullParameter() {
		assertEquals("Converter running with null values should return empty.", "", conv.getAsString(null, null, null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringIllegalArgumentString() {
		assertEquals("Converter running with the wrong object type (String) values should return empty.", "",
				conv.getAsString(null, null, "Per Abich"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringIllegalArgumentInteger() {
		assertEquals("Converter running with the wrong object type (Integer) values should return empty.", "",
				conv.getAsString(null, null, 1));
	}

}
