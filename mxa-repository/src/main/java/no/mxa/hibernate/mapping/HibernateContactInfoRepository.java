package no.mxa.hibernate.mapping;

import no.mxa.dataaccess.ContactInfoRepository;
import no.mxa.dto.ContactInfoDTO;
import no.mxa.utils.hibernate.BaseHibernateRepository;

import org.hibernate.SessionFactory;

/**
 * HibernateRepository class for ContactInfo
 */
public class HibernateContactInfoRepository extends BaseHibernateRepository<ContactInfoDTO> implements ContactInfoRepository {

	/**
	 * 
	 * @param sessionFactory
	 *            the session creation object
	 */
	public HibernateContactInfoRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @return class name
	 */
	public String getDtoClassName() {
		return ContactInfoDTO.class.getName();
	}
}
