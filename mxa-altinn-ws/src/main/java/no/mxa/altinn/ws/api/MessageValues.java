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

/**
 * These values are required to generate a Correspondence.
 */
public interface MessageValues {

	/**
	 * @return the govOrgan
	 */
	String getGovOrgan();

	/**
	 * @return the languageCode
	 */
	String getLanguageCode();

	/**
	 * @return the altinnPassword
	 */
	String getAltinnPassword();

	/**
	 * @return the mailFrom
	 */
	String getMailFrom();

	/**
	 * @return the notificationType
	 */
	String getNotificationType();

	/**
	 * @return serviceCode
	 */
	String getServiceCode();

	/**
	 * @return serviceEdition
	 */
	String getServiceEdition();

}
