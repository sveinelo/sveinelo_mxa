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
