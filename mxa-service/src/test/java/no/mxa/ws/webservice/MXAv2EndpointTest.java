/*
 * #%L
 * Service
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
package no.mxa.ws.webservice;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import no.mxa.ws.model.MessageRequest;
import no.mxa.ws.model.MessageResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-ws-context.xml")
public class MXAv2EndpointTest {
	private MXAv2Endpoint mxaV2;

	@Before
	public void setUp() throws Exception {
		mxaV2 = new MXAv2Endpoint();
	}

	@Test
	public void shouldReadSendingSystem() throws IOException {
		MessageRequest request = new MessageRequest();

		MessageResponse response = mxaV2.messageSubmit(request);

		assertThat(response.getStatus().getMessage(), is(equalTo("All is well.")));
	}
}
