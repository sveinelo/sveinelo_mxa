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
package no.mxa.web.model;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;

import org.joda.time.DateTime;
import org.junit.Test;

public class FooterModelTest {

	@Test
	public void footerModelShouldContainVersionNumberAndDateStarted() {
		String version = "1.4";

		FooterModel footerModel = new FooterModel(version);

		assertThat(footerModel.getVersion(), is(equalTo((version))));
		assertThat(footerModel.getDevelopedBy(), containsString(DateFormat.getDateInstance().format(DateTime.now().toDate())));
	}

}
