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
