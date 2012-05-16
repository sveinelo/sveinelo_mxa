package no.mxa.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import no.mxa.UniversalConstants;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviationTypeConverter implements Converter {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageStatusConverter.class);

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// Not implemented
		throw new NotImplementedException();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		if (object == null) {
			LOGGER.debug("Got a 'null' object.");
			return "";
		} else if (!(object instanceof Integer)) {
			throw new IllegalArgumentException("'object' needs to be of type 'Integer'");
		}

		Integer messageStatus = (Integer) object;
		if (messageStatus == UniversalConstants.MSG_STATUS_SEND_ALTINN_FAILED) {
			return UniversalConstants.GUI_DEVIATION_SEND_ALTINN_FAILED;
		} else {
			return UniversalConstants.GUI_DEVIATION_OVERDUE_READ_DEADLINE;
		}
	}
}
