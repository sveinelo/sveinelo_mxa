package no.mxa.service.implementations;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import no.mxa.dataaccess.AttachmentRepository;
import no.mxa.dto.AttachmentDTO;
import no.mxa.service.AttachmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class AttachmentServiceImpl implements AttachmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentServiceImpl.class);

	private AttachmentRepository attachmentRepository;

	@Inject
	public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}

	@Transactional
	@Override
	public AttachmentDTO searchById(Long id) {
		return this.attachmentRepository.findById(id);
	}

	@Transactional
	@Override
	public List<AttachmentDTO> searchByProperty(String propertyName, Object value) {
		return this.attachmentRepository.findByProperty(propertyName, value);
	}

	@Transactional
	@Override
	public byte[] getAttachmentAsByteArray(Long id) throws SQLException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Get attachmentDTO");
		AttachmentDTO attachmentDTO = searchById(id);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Get attachmentClob");
		Clob attachmentClob = attachmentDTO.getAttachment();

		int length = (int) attachmentClob.length();
		String attachmentClobAsString = attachmentClob.getSubString(1L, length);
		byte[] attachmentClobAsByteArray = attachmentClobAsString.getBytes();
		return decodeBase64(attachmentClobAsByteArray);
	}
}