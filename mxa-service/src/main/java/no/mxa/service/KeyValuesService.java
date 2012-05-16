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