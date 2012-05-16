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
		assertEquals("Converter running with null values should return empty.", DATE_STRING_VALUE,
				conv.getAsString(null, null, date));
	}

	@Test
	public void testGetAsStringNullParameter() {
		assertEquals("Converter running with null values should return empty.", "", conv.getAsString(null, null, null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringIllegalArgumentString() {
		assertEquals("Converter running with null values should return empty.", "", conv.getAsString(null, null, "Per Abich"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAsStringIllegalArgumentInteger() {
		assertEquals("Converter running with null values should return empty.", "", conv.getAsString(null, null, 1));
	}

}
