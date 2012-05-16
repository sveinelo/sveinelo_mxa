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
