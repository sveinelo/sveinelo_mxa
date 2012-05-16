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
