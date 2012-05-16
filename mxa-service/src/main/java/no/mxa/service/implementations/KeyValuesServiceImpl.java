package no.mxa.service.implementations;

import java.util.List;

import javax.inject.Inject;

import no.mxa.dataaccess.KeyValuesRepository;
import no.mxa.dto.KeyValuesDTO;
import no.mxa.service.KeyValuesService;

import org.springframework.transaction.annotation.Transactional;

public class KeyValuesServiceImpl implements KeyValuesService {
	private KeyValuesRepository keyValuesRepository;

	@Inject
	public KeyValuesServiceImpl(KeyValuesRepository keyValuesRepository) {
		this.keyValuesRepository = keyValuesRepository;
	}

	@Transactional
	@Override
	public KeyValuesDTO searchById(Long id) {
		return this.keyValuesRepository.findById(id);
	}

	@Transactional
	@Override
	public List<KeyValuesDTO> searchByProperty(String propertyName, Object value) {
		return this.keyValuesRepository.findByProperty(propertyName, value);
	}
}