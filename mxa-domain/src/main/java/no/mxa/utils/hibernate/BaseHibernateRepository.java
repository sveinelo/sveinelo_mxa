package no.mxa.utils.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Basic repository object for generic types. This object can be extended to handle both generic types or specific types: <br/>
 * <code>GenericRepository&lt;T&gt;
 * extends BaseHibernateRepository&lt;T&gt;</code> <br/>
 * or <br/>
 * <code>IntegerRepository
 * extends BaseHibernateRepository&lt;Integer&gt;</code>
 * 
 * This component does not handle transactions itself. It's recommended to use Spring or some other transactionmanagement.
 * 
 * @param <T>
 *            The type this repository is for
 * @param <N>
 *            The type of the id this repositories DTO uses
 */
public abstract class BaseHibernateRepository<T> implements IBaseHibernateRepository<T> {

	/**
	 * The session factory used by this instance.
	 */
	private SessionFactory sessionFactory;

	/**
	 * Constructs a BaseHibernateRepository with given sessionFactory.
	 * 
	 * @param sessionFactory
	 *            the session factory used by this instance.
	 */
	public BaseHibernateRepository(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public abstract String getDtoClassName();

	@SuppressWarnings("unchecked")
	public T findById(Serializable id) {
		return (T) getSessionFactory().getCurrentSession().get(getDtoClassName(), id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T instance) {
		return getSessionFactory().getCurrentSession().createCriteria(getDtoClassName()).add(Example.create(instance)).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByProperty(String propertyName, Object value) {
		String queryString = "from " + getDtoClassName() + " as model where model." + propertyName + "= ?";
		Query queryObject = getSessionFactory().getCurrentSession().createQuery(queryString);
		queryObject.setParameter(0, value);
		return queryObject.list();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void save(final T transientInstance) {
		getSessionFactory().getCurrentSession().save(transientInstance);
	}

	public void delete(final T persistentInstance) {
		getSessionFactory().getCurrentSession().delete(persistentInstance);
	}

	public void attachDirty(final T instance) {
		getSessionFactory().getCurrentSession().saveOrUpdate(instance);
	}

	public void attachClean(final T instance) {
		getSessionFactory().getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
	}

	@SuppressWarnings("unchecked")
	public T merge(final T detachedInstance) {
		return (T) getSessionFactory().getCurrentSession().merge(detachedInstance);
	}
}
