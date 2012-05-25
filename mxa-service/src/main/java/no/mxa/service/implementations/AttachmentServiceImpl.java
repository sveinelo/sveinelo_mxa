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
package no.mxa.service.implementations;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

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

	private final AttachmentRepository attachmentRepository;

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
	public byte[] getAttachmentAsByteArray(Long id) {
		LOGGER.debug("Get attachmentDTO");
		AttachmentDTO attachmentDTO = searchById(id);
		return decodeBase64(attachmentDTO.getBase64EncodedAttachement());
	}
}
