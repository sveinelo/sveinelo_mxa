/*
 * #%L
 * Altinn Webservice
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
package no.mxa.altinn.ws.api;

import static org.mockito.Mockito.when;
import no.mxa.altinn.ws.InsertCorrespondenceV2;
import no.mxa.dto.MessageDTO;

/**
 * Provide {@link InsertCorrespondenceV2} test data.
 * 
 */
public class MockedCorrespondenceBuilder {
	private CorrespondenceBuilder correspondenceBuilder;
	private MockedMessageDTOBuilder mockedMessageDtoBuilder;
	private MessageValues messageValues;

	protected InsertCorrespondenceV2 buildCorrespondence(MessageDTO message) throws CorrespondenceBuilderException {
		mockedMessageDtoBuilder = new MockedMessageDTOBuilder();
		correspondenceBuilder = new CorrespondenceBuilder(messageValues);
		configureMockValues();
		return correspondenceBuilder.buildInsertCorrespondenceV2FromMessageDTO(message);
	}

	private void configureMockValues() {
		when(messageValues.getGovOrgan()).thenReturn(mockedMessageDtoBuilder.getGovOrgan());
		when(messageValues.getLanguageCode()).thenReturn(mockedMessageDtoBuilder.getLanguageCode());
		when(messageValues.getMailFrom()).thenReturn(mockedMessageDtoBuilder.getSenderEmailAddress());
		when(messageValues.getNotificationType()).thenReturn(mockedMessageDtoBuilder.getNotificationType());
	}

	protected void setMessageValues(MessageValues messageValues) {
		this.messageValues = messageValues;
	}

}
