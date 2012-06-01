/*
 * #%L
 * Service
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
package no.mxa.ws.webservice;

import java.io.IOException;
import java.math.BigInteger;

import no.mxa.ws.model.MessageRequest;
import no.mxa.ws.model.MessageResponse;
import no.mxa.ws.model.MessageResponse.Status;
import no.mxa.ws.model.ObjectFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class MXAv2Endpoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(MXAv2Endpoint.class);
	private static final String NAMESPACE_URI = "http://mxa.no/schemas";
	private final ObjectFactory factory = new ObjectFactory();

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "MessageRequest")
	@ResponsePayload
	public MessageResponse messageSubmit(@RequestPayload
	MessageRequest messageRequest) throws IOException {
		processMessageRequest(messageRequest);

		MessageResponse responseType = factory.createMessageResponse();
		Status status = factory.createMessageResponseStatus();
		status.setCode(BigInteger.valueOf(0));
		status.setMessage("All is well.");
		responseType.setStatus(status);

		return responseType;
	}

	private void processMessageRequest(MessageRequest message) {
		LOGGER.warn("Got this Message: {}", message.getParticipantId());

	}
}
