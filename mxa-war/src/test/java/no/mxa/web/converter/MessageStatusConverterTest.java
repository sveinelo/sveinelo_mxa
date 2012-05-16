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
