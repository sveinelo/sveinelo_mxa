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
import static org.junit.Assert.fail;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class YesNoConverterTest {

	private YesNoConverter ynConv;

	@Before
	public void setup() {
		ynConv = new YesNoConverter();
	}

	@Test(expected = NotImplementedException.class)
	public void testGetAsObjectIsNotImplemented() {
		ynConv.getAsObject(null, null, null);
		fail("Expected a NotImplementedException to be thrown");
	}

	@Test(expected = NullPointerException.class)
	public void testGetAsStringNPE() {
		ynConv.getAsString(null, null, null);
	}

	@Test()
	public void testGetAsStringJa() {
		String actual = ynConv.getAsString(null, null, 1);
		assertEquals("Integer 1 skal være Ja.", "Ja", actual);
	}

	@Test()
	public void testGetAsStringNei() {
		String actual = ynConv.getAsString(null, null, 0);
		assertEquals("Verdier som ikke er 1 skal være Nei.", "Nei", actual);
	}

}
