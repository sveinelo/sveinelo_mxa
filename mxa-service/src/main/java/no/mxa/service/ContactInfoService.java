package no.mxa.service;

import java.util.List;

import no.mxa.dto.ContactInfoDTO;

public interface ContactInfoService {

	/**
	 * 
	 * @param id
	 *            The id of the ContactInfoDTO to retrieve
	 * @return The matching contactInfo DTO
	 */
	ContactInfoDTO searchById(Long id);

	/**
	 * 
	 * @param propertyName
	 *            The name of the property, e.g messageId
	 * @param value
	 *            The value of the property
	 * @return A list of contactInfoDTOs
	 */
	List<ContactInfoDTO> searchByProperty(String propertyName, Object value);
}