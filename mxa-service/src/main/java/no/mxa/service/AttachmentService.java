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

import java.sql.SQLException;
import java.util.List;

import no.mxa.dto.AttachmentDTO;

public interface AttachmentService {

	/**
	 * 
	 * @param id
	 *            The id of the AttachmentDTO to retrieve
	 * @return The matching attachment DTO
	 */
	AttachmentDTO searchById(Long id);

	/**
	 * 
	 * @param propertyName
	 *            The name of the property, e.g messageId
	 * @param value
	 *            The value of the property
	 * @return A list of attachmentDTOs
	 */
	List<AttachmentDTO> searchByProperty(String propertyName, Object value);

	/**
	 * 
	 * @param id
	 *            attachmentId
	 * @return byteArray representation of a Clob attachment.
	 * @throws SQLException
	 */
	byte[] getAttachmentAsByteArray(Long id) throws SQLException;

}
