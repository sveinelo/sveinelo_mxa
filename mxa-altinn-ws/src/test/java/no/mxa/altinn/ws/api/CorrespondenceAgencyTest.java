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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasic;
import no.mxa.altinn.ws.InsertCorrespondenceV2;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.altinn.ws.ReceiptStatusEnum;
import no.mxa.dto.MessageDTO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test the {@link CorrespondenceAgency} class.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class CorrespondenceAgencyTest {
	@Mock
	private MessageValues messageValues;
	@Mock
	private CorrespondenceBuilder correspondenceBuilder;
	@Mock
	private ICorrespondenceAgencyExternalBasic port;
	private MessageDTO message;
	private InsertCorrespondenceV2 insertCorrespondenceV2;
	private MockedMessageDTOBuilder mockedMessageDtoBuilder;
	private MockedCorrespondenceBuilder mockedCorrespondenceBuilder;
	private CorrespondenceAgency correspondenceAgency;
	private ReceiptExternal receipt;

	@Before
	public void setup() throws Exception {
		receipt = new ReceiptExternal();
		receipt.setReceiptStatusCode(ReceiptStatusEnum.OK);
		correspondenceAgency = new CorrespondenceAgency(messageValues, port);
		configureMockValues();
		mockedMessageDtoBuilder = new MockedMessageDTOBuilder();
		mockedCorrespondenceBuilder = new MockedCorrespondenceBuilder();
		mockedCorrespondenceBuilder.setMessageValues(messageValues);
		message = mockedMessageDtoBuilder.buildMessage();
		message.setMessageReference("UniqueMessageReference");
		insertCorrespondenceV2 = mockedCorrespondenceBuilder.buildCorrespondence(message);
	}

	@Test
	public void shouldCallAltinnWebServiceWithCorrectParameters() throws Exception {
		ArgumentCaptor<String> systemUserName = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> systemPassword = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> systemUserCode = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> externalShipmentReference = ArgumentCaptor.forClass(String.class);

		ArgumentCaptor<InsertCorrespondenceV2> correspondenceArgument = ArgumentCaptor.forClass(InsertCorrespondenceV2.class);
		correspondenceAgency.sendMessage(message);
		verify(port, times(1)).insertCorrespondenceBasicV2(systemUserName.capture(), systemPassword.capture(),
				systemUserCode.capture(), externalShipmentReference.capture(), correspondenceArgument.capture());
		assertThat(systemUserName.getValue(), is("SER"));
		assertThat(systemPassword.getValue(), is("Password"));
		assertThat(externalShipmentReference.getValue(), is("UniqueMessageReference"));
		assertThat(correspondenceArgument.getValue().getReportee().getValue(), is("0101010101010"));
	}

	private void configureMockValues() throws Exception {
		when(messageValues.getAltinnPassword()).thenReturn("Password");
		when(messageValues.getSystemUserName()).thenReturn("SER");
		when(correspondenceBuilder.buildInsertCorrespondenceV2FromMessageDTO(message)).thenReturn(insertCorrespondenceV2);
		when(
				port.insertCorrespondenceBasicV2(anyString(), anyString(), anyString(), anyString(),
						any(InsertCorrespondenceV2.class))).thenReturn(receipt);
	}

}
