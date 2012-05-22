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
