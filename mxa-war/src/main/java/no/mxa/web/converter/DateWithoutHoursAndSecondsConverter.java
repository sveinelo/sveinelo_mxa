package no.mxa.web.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateWithoutHoursAndSecondsConverter implements Converter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateWithoutHoursAndSecondsConverter.class);

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// Not implemented
		throw new NotImplementedException();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 == null) {
			LOGGER.debug("Null passed into getAsString.");
			return "";
		} else if (!(arg2 instanceof Date)) {
			throw new IllegalArgumentException("this method cannot handle objects of type " + arg2.getClass().getName());
		}
		Date today = (Date) arg2;
		String pattern = "yyyy.MM.dd";
		Locale currentLocale = Locale.getDefault();
		String output;
		SimpleDateFormat formatter;

		formatter = new SimpleDateFormat(pattern, currentLocale);
		output = formatter.format(today);

		return output;
	}

}
