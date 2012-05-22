/*
 * #%L
 * Repository
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
package no.mxa.hibernate.mapping;

import no.mxa.dataaccess.AttachmentRepository;
import no.mxa.dto.AttachmentDTO;
import no.mxa.utils.hibernate.BaseHibernateRepository;

import org.hibernate.SessionFactory;

/**
 * HibernateRepository class for Attachment
 */
public class HibernateAttachmentRepository extends BaseHibernateRepository<AttachmentDTO> implements AttachmentRepository {

	/**
	 * 
	 * @param sessionFactory
	 *            the session creation object
	 */
	public HibernateAttachmentRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @return class name
	 */
	public String getDtoClassName() {
		return AttachmentDTO.class.getName();
	}
}
