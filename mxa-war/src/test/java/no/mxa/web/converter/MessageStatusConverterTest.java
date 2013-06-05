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


import static no.mxa.UniversalConstants.GUI_MSG_STATUS_CONFIRMED_IN_ALTINN;
import static no.mxa.UniversalConstants.MSG_STATUS_CONFIRMED_IN_ALTINN;
import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class MessageStatusConverterTest {

	private MessageStatusConverter conv;

	@Before
	public void setUp() throws Exception {
		conv = new MessageStatusConverter();
	}

	@Test(expected = NotImplementedException.class)
	public void testGetAsObject() {
		conv.getAsObject(null, null, null);
	}

	@Test
	public void testGetAsString() {
		assertEquals("All null parameters should be fine.", "", conv.getAsString(null, null, null));
		assertEquals("Non-existing parameter should be fine.", "", conv.getAsString(null, null, "Per Abich"));
		assertEquals("Parameter should be translated", GUI_MSG_STATUS_CONFIRMED_IN_ALTINN,
				conv.getAsString(null, null, MSG_STATUS_CONFIRMED_IN_ALTINN));

	}

}
