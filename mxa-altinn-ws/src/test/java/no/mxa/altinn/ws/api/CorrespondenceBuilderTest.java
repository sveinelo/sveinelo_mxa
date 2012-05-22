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
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import no.mxa.altinn.ws.InsertCorrespondenceV2;
import no.mxa.dto.MessageDTO;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test the {@link CorrespondenceBuilder}.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class CorrespondenceBuilderTest {
	private CorrespondenceBuilder correspondenceBuilder;
	@Mock
	private MessageValues messageValues;
	private MessageDTO message;
	private MockedMessageDTOBuilder mockedMessageDtoBuilder;

	@Before
	public void setup() throws SerialException, SQLException {
		correspondenceBuilder = new CorrespondenceBuilder(messageValues);
		mockedMessageDtoBuilder = new MockedMessageDTOBuilder();
		message = mockedMessageDtoBuilder.buildMessage();
		configureMockValues();
	}

	@Test
	public void shouldBuildInsertCorrespondenceV2FromMessageDTO() throws CorrespondenceBuilderException {
		InsertCorrespondenceV2 insertCorrespondenceV2 = correspondenceBuilder
				.buildInsertCorrespondenceV2FromMessageDTO(message);
		assertThat(insertCorrespondenceV2, is(notNullValue()));

		/** Correspondence */
		assertCorrespondence(insertCorrespondenceV2);
		/** Content */
		assertContent(insertCorrespondenceV2);
		/** Attachments */
		assertAttachments(insertCorrespondenceV2);
		/** Notifications */
		assertNotifications(insertCorrespondenceV2);
	}

	/** Correspondences.Correspondence */
	private void assertCorrespondence(InsertCorrespondenceV2 insertCorrespondenceV2) {
		assertThat(insertCorrespondenceV2.getServiceCode().getValue(), is(mockedMessageDtoBuilder.getGovOrgan()));
		assertThat(insertCorrespondenceV2.getServiceEdition().getValue(), is(mockedMessageDtoBuilder.getServiceEdition()));
		assertThat(insertCorrespondenceV2.getReportee().getValue(), is(mockedMessageDtoBuilder.getParticipantId()));
		assertThat(insertCorrespondenceV2.getDueDateTime().getYear(), is(new DateTime().getYear()));
		assertThat(insertCorrespondenceV2.getDueDateTime().getMonth(), is(new DateTime().getMonthOfYear()));
		assertThat(insertCorrespondenceV2.getDueDateTime().getDay(), is(new DateTime().getDayOfMonth() + 7));
		assertThat(insertCorrespondenceV2.getAllowSystemDeleteDateTime().getValue().getYear(), is(new DateTime().getYear() + 5));
		assertThat(insertCorrespondenceV2.getAllowSystemDeleteDateTime().getValue().getMonth(),
				is(new DateTime().getMonthOfYear()));
		assertThat(insertCorrespondenceV2.getAllowSystemDeleteDateTime().getValue().getDay(),
				is(new DateTime().getDayOfMonth()));
		assertThat(insertCorrespondenceV2.getArchiveReference().getValue(), is(mockedMessageDtoBuilder.getAltinnArchive()));
	}

	/** Correspondences.Correspondence.Content */
	private void assertContent(InsertCorrespondenceV2 insertCorrespondenceV2) {
		assertThat(insertCorrespondenceV2.getContent().getValue().getLanguageCode().getValue(),
				is(mockedMessageDtoBuilder.getLanguageCode()));
		assertThat(insertCorrespondenceV2.getContent().getValue().getMessageTitle().getValue(),
				is(mockedMessageDtoBuilder.getMessageHeader()));
		assertThat(insertCorrespondenceV2.getContent().getValue().getMessageBody().getValue(),
				is(mockedMessageDtoBuilder.getMessageSummary()));
	}

	/** Correspondences.Correspondence.Content.Attachments.BinaryAttachments */
	private void assertAttachments(InsertCorrespondenceV2 insertCorrespondenceV2) {
		assertThat(insertCorrespondenceV2.getContent().getValue().getAttachments().getValue().getBinaryAttachments().getValue()
				.getBinaryAttachmentV2().size(), is(2));
		assertThat(insertCorrespondenceV2.getContent().getValue().getAttachments().getValue().getBinaryAttachments().getValue()
				.getBinaryAttachmentV2().get(0).getFileName().getValue(), is(mockedMessageDtoBuilder.getFileName1()));
		assertThat(insertCorrespondenceV2.getContent().getValue().getAttachments().getValue().getBinaryAttachments().getValue()
				.getBinaryAttachmentV2().get(1).getFileName().getValue(), is(mockedMessageDtoBuilder.getFileName2()));
	}

	/** Correspondences.Correspondence.Notifications */
	private void assertNotifications(InsertCorrespondenceV2 insertCorrespondenceV2) {
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().size(), is(1));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getReceiverEndPoints()
				.getValue().getReceiverEndPoint().size(), is(2));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getFromAddress().getValue(),
				is(mockedMessageDtoBuilder.getSenderEmailAddress()));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getLanguageCode().getValue(),
				is(mockedMessageDtoBuilder.getLanguageCode()));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getNotificationType()
				.getValue(), is(mockedMessageDtoBuilder.getNotificationType()));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getReceiverEndPoints()
				.getValue().getReceiverEndPoint().get(0).getTransportType().getValue().toString(), is("SMS"));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getReceiverEndPoints()
				.getValue().getReceiverEndPoint().get(0).getReceiverAddress().getValue(),
				is(mockedMessageDtoBuilder.getPhoneNumber()));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getReceiverEndPoints()
				.getValue().getReceiverEndPoint().get(1).getTransportType().getValue().toString(), is("EMAIL"));
		assertThat(insertCorrespondenceV2.getNotifications().getValue().getNotification().get(0).getReceiverEndPoints()
				.getValue().getReceiverEndPoint().get(1).getReceiverAddress().getValue(),
				is(mockedMessageDtoBuilder.getEmailAddress()));
	}

	private void configureMockValues() {
		when(messageValues.getGovOrgan()).thenReturn(mockedMessageDtoBuilder.getGovOrgan());
		when(messageValues.getLanguageCode()).thenReturn(mockedMessageDtoBuilder.getLanguageCode());
		when(messageValues.getMailFrom()).thenReturn(mockedMessageDtoBuilder.getSenderEmailAddress());
		when(messageValues.getNotificationType()).thenReturn(mockedMessageDtoBuilder.getNotificationType());
		when(messageValues.getServiceCode()).thenReturn(mockedMessageDtoBuilder.getServiceCode());
		when(messageValues.getServiceEdition()).thenReturn(mockedMessageDtoBuilder.getServiceEdition());
	}

}
