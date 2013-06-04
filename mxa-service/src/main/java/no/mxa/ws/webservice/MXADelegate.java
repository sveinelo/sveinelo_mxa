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

import javax.inject.Inject;
import javax.jws.WebService;

@WebService(targetNamespace = "http://webservice.ws.altut.patent.siriusit.com/", serviceName = "MXA", portName = "MXAPort", wsdlLocation = "no/mxa/ws/xml/wsdl/MXA.wsdl")
public class MXADelegate {
	@Inject
	private MXA mxa;

	public int submitMessage(String message) {
		return mxa.submitMessage(message);
	}

}
