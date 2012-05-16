package no.mxa.service.implementations;

import java.util.List;

import javax.inject.Inject;

import no.mxa.dataaccess.ContactInfoRepository;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.service.ContactInfoService;

import org.springframework.transaction.annotation.Transactional;

public class ContactInfoServiceImpl implements ContactInfoService {

	private ContactInfoRepository contactInfoRepository;

	@Inject
	public ContactInfoServiceImpl(ContactInfoRepository contactInfoRepository) {
		this.contactInfoRepository = contactInfoRepository;
	}

	@Transactional
	@Override
	public ContactInfoDTO searchById(Long id) {
		return this.contactInfoRepository.findById(id);
	}

	@Transactional
	@Override
	public List<ContactInfoDTO> searchByProperty(String propertyName, Object value) {
		return this.contactInfoRepository.findByProperty(propertyName, value);
	}
}