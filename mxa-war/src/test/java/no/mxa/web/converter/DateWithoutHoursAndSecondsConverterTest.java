/*
 * #%L
 * Web Archive
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
package no.mxa.web.converter;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class DateWithoutHoursAndSecondsConverterTest {
	private static final long DATE_LONG_VALUE = 1336397812271L;
	private static final String DATE_STRING_VALUE = "2012.05.07";
	private DateWithoutHoursAndSecondsConverter conv;
	private Date date;

	@Before
	public void setUp() throws Exception {
		conv = new DateWithoutHoursAndSecondsConverter();
		date = new Date(DATE_LONG_VALUE);
	}

	@Test(expected = NotImplementedException.class)
	public void testGetAsObject() {
		conv.getAsObject(null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringStringArgument() {
		conv.getAsString(null, null, "Per Abich");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringIntegerArgument() {
		conv.getAsString(null, null, 1);
	}

	@Test
	public void testGetAsStringValidArguments() {
		assertEquals("Test if null parameters return empty date", "", conv.getAsString(null, null, null));
		assertEquals("Input date should return as String without time.", DATE_STRING_VALUE, conv.getAsString(null, null, date));
	}

}
