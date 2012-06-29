/*
 * #%L
 * Domain
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
package no.mxa.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

public final class DateUtils {
	private DateUtils() {
	}

	/**
	 * @param readDeadline
	 * @param daysToAdd
	 * @return readDeadline+daysToAdd
	 */
	public static Date getFutureDate(final Date readDeadline, final int daysToAdd) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(readDeadline);
		calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
		return calendar.getTime();
	}

	/**
	 * Returns a future XMLGregorianCalendar
	 * 
	 * @param field
	 *            - Calendar.YEAR, Calendar.MONTH or Calendar.DAY_OF_MONTH
	 * @param number
	 *            - number of units of the given field type to add
	 * @param addedStartDays
	 *            - number of days to add before adding the other units
	 * 
	 * @return XMLGregorianCalender
	 */
	public static XMLGregorianCalendar getFutureDate(int field, int number, int addedStartDays)
			throws DatatypeConfigurationException {
		GregorianCalendar today = (GregorianCalendar) Calendar.getInstance();
		today.add(Calendar.DAY_OF_MONTH, addedStartDays);
		today.add(field, number);
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
				new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)));

		return cal;
	}

	public static XMLGregorianCalendar getToday() throws DatatypeConfigurationException {
		DateTime nowPlusFiveMinutes = new DateTime().plusMinutes(5);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(nowPlusFiveMinutes.toGregorianCalendar());
	}

}
