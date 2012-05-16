package no.mxa.utils.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

/**
 * Data access interface for hibernate.
 * 
 */
public interface IBaseHibernateRepository<T> {

	/**
	 * Retrieves the session factory associated with this repository.
	 * 
	 * @return the session factory associated with this repository.
	 */
	SessionFactory getSessionFactory();

	/**
	 * Retrieves a abritary persistent object with id based on class name
	 * 
	 * @param String
	 *            the id of the persistent object to retrieve.
	 * @return The matching object
	 */
	T findById(Serializable id);

	/**
	 * Finds objects by example, one to many properties to match
	 * 
	 * @param instance
	 *            The Criteria object to be matched
	 * @return The list of the matching objects
	 */
	List<T> findByExample(final T criteria);

	/**
	 * Finds objects by property
	 * 
	 * @param propertyName
	 *            The name of the property to match
	 * @param value
	 *            The value of the object to match
	 * @return The list of the matching object
	 */
	List<T> findByProperty(String propertyName, Object value);

	/**
	 * Saves an unsaved object.
	 * 
	 * @param transientInstance
	 *            the unsaved object to save.
	 */
	void save(final T transientInstance);

	/**
	 * Deletes an existing object.
	 * 
	 * @param persistentInstance
	 *            the object to delete.
	 */
	void delete(final T persistentInstance);

	/**
	 * Saves or updates an object, existing or not.
	 * 
	 * @param instance
	 *            The object to save/update.
	 */
	void attachDirty(final T instance);

	/**
	 * Re-associate a transient instance with a session.
	 * 
	 * @param instance
	 *            The object to re-associate with the session.
	 */
	void attachClean(final T instance);

	/**
	 * Copies the state of the object into an persistent instance and return the persistent instance. If the given instance is
	 * unsaved, save a copy of and return it as a newly persistent instance.
	 * 
	 * @param detachedInstance
	 *            the object to merge into persistent instance.
	 * @return The persistent instance.
	 */
	T merge(final T detachedInstance);
}
