/*
 * #%L
 * Domain
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
package no.mxa.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.input.BOMInputStream;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class UnicodeUtilTest {

	private static final String TEST_AS_HEX = "3c 3f 78 6d 6c 20 76 65 72 73 69 6f 6e 3d 22 31 2e 30 22 20 65 "
			+ "6e 63 6f 64 69 6e 67 3d 22 55 54 46 2d 38 22 3f 3e 3c 74 65 73 74 3e 3c 2f 74 65 73 74 3e ";
	private String testString;
	private String bomString;

	@Before
	public void setUp() throws Exception {
		testString = "Dette er en Test";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
		out.write(testString.getBytes());
		out.close();
		Resource bomTest = new ClassPathResource("BOMTest.xml");
		InputStream in = bomTest.getInputStream();
		BOMInputStream uis = new BOMInputStream(in, true);
		BufferedReader reader = new BufferedReader(new InputStreamReader(uis));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();
		bomString = sb.toString();
		testString = bomString.substring(bomString.indexOf("<"));
	}

	@Test
	public void construcedStringsShouldHaveRightBytes() {
		assertEquals("Test String needs to be plain UTF-8", TEST_AS_HEX, convertString2Hex(testString).toString());
		assertEquals("The Bom String needs to have the same content as TestString but with additional BOM",
				"ffffffef ffffffbb ffffffbf " + TEST_AS_HEX, convertString2Hex(bomString).toString());
	}

	@Test
	public void testDecode() throws Exception {
		String decodedString = UnicodeUtil.decode(bomString, "UTF-8");
		assertThat(decodedString, is(equalTo(testString)));
		assertThat(convertString2Hex(decodedString).toString(), is(equalTo(TEST_AS_HEX)));
		assertArrayEquals(testString.getBytes(), decodedString.getBytes());
	}

	@Test
	public void verifyThatConvertMakesRightString() {
		byte[] convertedString = UnicodeUtil.convert(testString.getBytes(), "UTF-8");
		assertThat(convertedString, is(equalTo(bomString.getBytes())));
	}

	private StringBuilder convertString2Hex(String inputString) {
		final StringBuilder firstCharsHEX = new StringBuilder();
		for (final byte b : inputString.getBytes()) {
			firstCharsHEX.append(Integer.toHexString(b)).append(" ");
		}
		return firstCharsHEX;
	}

}
