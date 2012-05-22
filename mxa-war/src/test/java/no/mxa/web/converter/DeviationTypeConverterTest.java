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

import static no.mxa.UniversalConstants.GUI_DEVIATION_OVERDUE_READ_DEADLINE;
import static no.mxa.UniversalConstants.GUI_DEVIATION_SEND_ALTINN_FAILED;
import static no.mxa.UniversalConstants.MSG_STATUS_SEND_ALTINN_FAILED;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class DeviationTypeConverterTest {

	private DeviationTypeConverter conv;

	@Before
	public void setUp() throws Exception {
		conv = new DeviationTypeConverter();
	}

	@Test(expected = NotImplementedException.class)
	public void testGetAsObject() {
		conv.getAsObject(null, null, null);
	}

	@Test
	public void testGetAsString() {
		assertEquals("Real values should translate correctly.", GUI_DEVIATION_SEND_ALTINN_FAILED,
				conv.getAsString(null, null, MSG_STATUS_SEND_ALTINN_FAILED));
		assertEquals("Unmapped values should translate to default value.", GUI_DEVIATION_OVERDUE_READ_DEADLINE,
				conv.getAsString(null, null, 1));
	}

	@Test
	public void testGetAsStringNullParameter() {
		assertEquals("Null as third parameter should return empty", "", conv.getAsString(null, null, null));

	}

	/*
	 * Invalid type throws you out.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringStringParameter() {
		conv.getAsString(null, null, "Per Abich");
	}

}
