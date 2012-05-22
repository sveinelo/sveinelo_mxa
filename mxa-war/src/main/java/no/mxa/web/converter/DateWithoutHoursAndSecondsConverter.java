/*
 * #%L
 * Web Archive
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
