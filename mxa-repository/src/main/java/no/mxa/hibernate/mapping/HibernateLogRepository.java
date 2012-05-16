package no.mxa.hibernate.mapping;

import java.util.List;

import no.mxa.dataaccess.LogRepository;
import no.mxa.dto.LogDTO;
import no.mxa.utils.hibernate.BaseHibernateRepository;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * HibernateRepository class for Log
 */
public class HibernateLogRepository extends BaseHibernateRepository<LogDTO> implements LogRepository {

	/**
	 * 
	 * @param sessionFactory
	 *            the session creation object
	 */
	public HibernateLogRepository(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/**
	 * @return class name
	 */
	public String getDtoClassName() {
		return LogDTO.class.getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogDTO> findAllLogsWithNullInMessageId() {
		return getSessionFactory().getCurrentSession().createCriteria(this.getDtoClassName())
				.add(Restrictions.isNull("message")).addOrder(Order.desc("time")).setMaxResults(100).list();
	}
}
