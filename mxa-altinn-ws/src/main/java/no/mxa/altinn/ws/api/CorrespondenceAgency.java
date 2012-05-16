package no.mxa.altinn.ws.api;

import java.net.MalformedURLException;

import javax.inject.Inject;

import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasic;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.InsertCorrespondenceV2;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.dto.MessageDTO;

/**
 * Builds a Correspondence message and calls webservice in Altinn.
 */
public class CorrespondenceAgency implements AltinnWS {
	private MessageValues messageValues;
	private CorrespondenceBuilder builder;
	private ICorrespondenceAgencyExternalBasic port;

	/**
	 * Constructor.
	 * 
	 * @param messageValues
	 *            the {@link MessageValues} to collect static values from
	 * @param port
	 *            the {@link ICorrespondenceAgencyExternalBasic} port to use
	 */
	@Inject
	public CorrespondenceAgency(MessageValues messageValues, ICorrespondenceAgencyExternalBasic port) {
		this.messageValues = messageValues;
		builder = new CorrespondenceBuilder(messageValues);
		this.port = port;
	}

	@Override
	public ReceiptExternal sendMessage(MessageDTO message) throws MalformedURLException,
			ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage,
			CorrespondenceBuilderException {
		InsertCorrespondenceV2 correspondence = builder.buildInsertCorrespondenceV2FromMessageDTO(message);
		ReceiptExternal receipt = callWebService(correspondence, message);
		return receipt;
	}

	/**
	 * Calls webservice in Altinn.
	 * 
	 * @param correspondence
	 *            the {@link InsertCorrespondenceV2} to send.
	 * @throws MalformedURLException
	 * @throws ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage
	 */
	private ReceiptExternal callWebService(InsertCorrespondenceV2 correspondence, MessageDTO message)
			throws MalformedURLException,
			ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage {
		String systemUserName = getSystemUserName();
		String systemPassword = getSystemPassword();
		String systemUserCode = getSystemUserCode();
		String externalShipmentReference = getExternalShipmentReference(message);

		ReceiptExternal receipt = port.insertCorrespondenceBasicV2(systemUserName, systemPassword, systemUserCode,
				externalShipmentReference, correspondence);

		return receipt;
	}

	private String getSystemUserCode() {
		return messageValues.getServiceCode();
	}

	private String getSystemPassword() {
		return messageValues.getAltinnPassword();
	}

	private String getSystemUserName() {
		return messageValues.getGovOrgan();
	}

	private String getExternalShipmentReference(MessageDTO message) {
		return message.getMessageReference();
	}

}
