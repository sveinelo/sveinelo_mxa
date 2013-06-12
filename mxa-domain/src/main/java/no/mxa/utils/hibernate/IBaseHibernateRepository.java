/*
 * #%L
 * Domain
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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
