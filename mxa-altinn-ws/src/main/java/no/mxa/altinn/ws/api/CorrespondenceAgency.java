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
