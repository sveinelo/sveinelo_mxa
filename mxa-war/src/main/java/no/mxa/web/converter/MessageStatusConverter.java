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
