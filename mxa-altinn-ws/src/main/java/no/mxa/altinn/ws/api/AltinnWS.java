package no.mxa.altinn.ws.api;

import java.net.MalformedURLException;

import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.dto.MessageDTO;

public interface AltinnWS {
	/**
	 * Creates a Correspondence message based on the input, and sends it to Altinn.
	 * 
	 * @param message
	 * @return
	 * @throws MalformedURLException
	 * @throws ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage
	 * @throws CorrespondenceBuilderException
	 */
	ReceiptExternal sendMessage(MessageDTO message) throws MalformedURLException,
			ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage,
			CorrespondenceBuilderException;

}
