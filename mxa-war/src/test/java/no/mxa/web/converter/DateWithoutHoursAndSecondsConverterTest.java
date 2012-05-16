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
		assertEquals("", conv.getAsString(null, null, null));
		assertEquals(DATE_STRING_VALUE, conv.getAsString(null, null, date));
	}

}
