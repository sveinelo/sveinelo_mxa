/*
 * #%L
 * Web Archive
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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
package no.mxa.web.converter;

import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import no.mxa.UniversalConstants;

import org.apache.commons.lang.NotImplementedException;

/**
 * Upon creation this converter creates a statusCodeMap <strong>which needs to be manually updated</strong> when adding new
 * message statuses to UniversalConstants.
 */
public class MessageStatusConverter implements Converter {

	private HashMap<Integer, String> statusCodeMap;

	public MessageStatusConverter() {
		// This Converter is not managed by the spring-application-context but by faces-config.xml context.
		// This constructor is then run unnecessary often. The converter should be a singelton instead of request scope.
		statusCodeMap = new HashMap<Integer, String>();
		statusCodeMap.put(UniversalConstants.MSG_STATUS_RECEIVED, UniversalConstants.GUI_MSG_STATUS_RECEIVED);
		statusCodeMap.put(UniversalConstants.MSG_STATUS_SENT_ALTINN, UniversalConstants.GUI_MSG_STATUS_SENT_ALTINN);
		statusCodeMap.put(UniversalConstants.MSG_STATUS_SEND_ALTINN_FAILED,
				UniversalConstants.GUI_MSG_STATUS_SEND_ALTINN_FAILED);
		statusCodeMap.put(UniversalConstants.MSG_STATUS_READ_IN_ALTINN, UniversalConstants.GUI_MSG_STATUS_READ_IN_ALTINN);
		statusCodeMap.put(UniversalConstants.MSG_STATUS_CONFIRMED_IN_ALTINN,
				UniversalConstants.GUI_MSG_STATUS_CONFIRMED_IN_ALTINN);
		statusCodeMap.put(UniversalConstants.MSG_STATUS_OVERDUEWARN_SENT_TO_AGENCY,
				UniversalConstants.GUI_MSG_STATUS_OVERDUEWARN_SENT_TO_AGENCY);
		statusCodeMap.put(UniversalConstants.MSG_STATUS_MANUALLY_REMOVED, UniversalConstants.GUI_MSG_STATUS_MANUALLY_REMOVED);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent components, String args) {
		// Not implemented
		throw new NotImplementedException();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent components, Object object) {
		if (statusCodeMap.containsKey(object)) {
			return statusCodeMap.get(object).toString();
		}
		return "";
	}

}
