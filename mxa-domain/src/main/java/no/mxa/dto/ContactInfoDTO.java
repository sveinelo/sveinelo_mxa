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
package no.mxa.dto;

import java.io.Serializable;

/**
 * DTO class which handles database mappings related to message contact info
 */
public class ContactInfoDTO implements Serializable {

	/**
	 * Unique generated version number
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * Private class variables
	 */
	private Long id;
	private MessageDTO message;
	private String type;
	private String address;

	/**
	 * Default constructor
	 */
	public ContactInfoDTO() {

	}

	/**
	 * Constructor which takes contact information as parameters
	 * 
	 * @param type
	 *            Type of contact information
	 * @param address
	 *            The contact information as an email address
	 */
	public ContactInfoDTO(String type, String address) {
		this.type = type;
		this.address = address;
	}

	/**
	 * Getter method for id
	 * 
	 * @return: id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for id
	 * 
	 * @param id
	 *            : The id value to set on the contact info
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for message
	 * 
	 * @return: message
	 */
	public MessageDTO getMessage() {
		return message;
	}

	/**
	 * Setter method for message
	 * 
	 * @param message
	 *            : The parent message
	 */
	public void setMessage(MessageDTO message) {
		this.message = message;
	}

	/**
	 * Getter method for type
	 * 
	 * @return: type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter method for type
	 * 
	 * @param type
	 *            : The contact info type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter method for address
	 * 
	 * @return: address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Setter method for address
	 * 
	 * @param address
	 *            : The contact email address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}
