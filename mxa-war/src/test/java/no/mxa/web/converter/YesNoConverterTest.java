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

	@Test
	public void testGetAsObjectIsNotImplemented() {
		try {
			ynConv.getAsObject(null, null, null);
			fail("Expected a NotImplementedException to be thrown");
		} catch (NotImplementedException e) {
		}
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
