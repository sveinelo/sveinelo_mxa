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