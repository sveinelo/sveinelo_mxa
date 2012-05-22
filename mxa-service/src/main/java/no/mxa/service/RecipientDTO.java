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
package no.mxa.service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

/**
 * Class used to send mail recipients to SendMail
 */
public class RecipientDTO {

	private InternetAddress address;

	// Type shall be of value defined in javax.mail.Message.RecipientType.*
	private RecipientType type;

	public RecipientDTO(RecipientType type, InternetAddress address) {
		this.type = type;
		this.address = address;
	}

	/**
	 * @return the recipient
	 */
	public InternetAddress getAddresst() {
		return address;
	}

	/**
	 * @param recipient
	 *            the recipient to set
	 */
	public void setAddress(InternetAddress address) {
		this.address = address;
	}

	/**
	 * @return the type
	 */
	public RecipientType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(RecipientType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return address.getAddress();
	}

}
