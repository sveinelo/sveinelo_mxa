package no.mxa.hibernate.mapping;

import no.mxa.dataaccess.KeyValuesRepository;
import no.mxa.dto.KeyValuesDTO;
import no.mxa.utils.hibernate.BaseHibernateRepository;

import org.hibernate.SessionFactory;

/**
 * HibernateRepository class for KeyValues
 */
public class HibernateKeyValuesRepository extends BaseHibernateRepository<KeyValuesDTO> implements KeyValuesRepository {

	/**
	 * 
	 * @param sessionFactory
	 *            the session creation object
	 */
	public HibernateKeyValuesRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @return class name
	 */
	public String getDtoClassName() {
		return KeyValuesDTO.class.getName();
	}
}
