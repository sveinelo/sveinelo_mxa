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

import java.util.List;

import no.mxa.dto.KeyValuesDTO;

public interface KeyValuesService {

	/**
	 * 
	 * @param propertyName
	 *            The name of the property, e.g messageId
	 * @param value
	 *            The value of the property
	 * @return A list of keyValuesDTOs
	 */
	List<KeyValuesDTO> searchByProperty(String propertyName, Object value);

	/**
	 * 
	 * @param id
	 *            The id of the KeyValuesDTO to retrieve
	 * @return The matching keyValues DTO
	 */
	KeyValuesDTO searchById(Long id);
}
