/*
 * #%L
 * Integration tests
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
package no.mxa.service.altut;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.inject.Inject;

import no.mxa.test.support.SpringBasedTest;

import org.hibernate.SessionFactory;
import org.junit.Test;

public class SendAltutMessageTest extends SpringBasedTest {
	@Inject
	SessionFactory sessionFactory;
	@Inject
	MessageSender sendAltutMessage;

	@Test
	public void shouldSendAltutMessageWithResultOk() {
		String testResult = "";
		String result = sendAltutMessage.sendMessages();

		if (result.indexOf("OK") > -1) {
			testResult = "OK";
		}
		assertThat(testResult, is("OK"));
	}

}
