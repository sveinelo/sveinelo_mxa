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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.mxa;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import no.mxa.utils.hibernate.BaseHibernateRepository;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session.LockRequest;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Example;
import org.junit.Test;

public class BaseHibernateRepositoryTest {

	class BaseHibernateRepositoryTestClass extends BaseHibernateRepository<String> {
		public BaseHibernateRepositoryTestClass(SessionFactory sessionFactory) {
			super(sessionFactory);
		}

		public String getDtoClassName() {
			return String.class.getName();
		}

	}

	/**
	 * Test of getSessionFactory method, of class BaseHibernateRepository.
	 */
	@Test
	public void testGetSessionFactory() {
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		SessionFactory expResult = sessionFactoryMock;
		SessionFactory result = instance.getSessionFactory();
		assertEquals(expResult, result);
	}

	/**
	 * Test of save method, of class BaseHibernateRepository.
	 */
	@Test
	public void testSave() {
		String string = "This is the expected String";

		// create mocks
		Session sessionMock = createMock(Session.class);
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);

		// set up expectations
		expect(sessionFactoryMock.getCurrentSession()).andReturn(sessionMock);
		expect(sessionMock.save(string)).andReturn(string);

		// prepare EasyMock
		replay(sessionMock, sessionFactoryMock);

		// execute
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		instance.save(string);

		// verify execution
		verify(sessionMock, sessionFactoryMock);
	}

	/**
	 * Test of delete method, of class BaseHibernateRepository.
	 */
	@Test
	public void testDelete() {
		String string = "This is the expected String";

		// create mocks
		Session sessionMock = createMock(Session.class);
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);

		// set up expectations
		expect(sessionFactoryMock.getCurrentSession()).andReturn(sessionMock);
		sessionMock.delete((Object) string);

		// prepare EasyMock
		replay(sessionMock, sessionFactoryMock);

		// execute
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		instance.delete(string);

		// verify execution
		verify(sessionMock, sessionFactoryMock);
	}

	/**
	 * Test of attachDirty method, of class BaseHibernateRepository.
	 */
	@Test
	public void testAttachDirty() {
		String string = "This is the expected String";

		// create mocks
		Session sessionMock = createMock(Session.class);
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);

		// set up expectations
		expect(sessionFactoryMock.getCurrentSession()).andReturn(sessionMock);
		sessionMock.saveOrUpdate(string);

		// prepare EasyMock
		replay(sessionMock, sessionFactoryMock);

		// execute
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		instance.attachDirty(string);

		// verify execution
		verify(sessionMock, sessionFactoryMock);
	}

	/**
	 * Test of attachClean method, of class BaseHibernateRepository.
	 */
	@Test
	public void testAttachClean() {
		String string = "This is the expected String";

		// create mocks
		Session sessionMock = createMock(Session.class);
		LockRequest lockRequest = createMock(LockRequest.class);
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);

		// set up expectations
		expect(sessionFactoryMock.getCurrentSession()).andReturn(sessionMock);
		expect(sessionMock.buildLockRequest(LockOptions.NONE)).andReturn(lockRequest);
		lockRequest.lock(string);

		// prepare EasyMock
		replay(sessionMock, sessionFactoryMock);

		// execute
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		instance.attachClean(string);

		// verify execution
		verify(sessionMock, sessionFactoryMock);
	}

	/**
	 * Test of merge method, of class BaseHibernateRepository.
	 */
	@Test
	public void testMerge() {
		String string = "This is the expected String";

		// create mocks
		Session sessionMock = createMock(Session.class);
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);

		// set up expectations
		expect(sessionFactoryMock.getCurrentSession()).andReturn(sessionMock);
		expect(sessionMock.merge(string)).andReturn(string);

		// prepare EasyMock
		replay(sessionMock, sessionFactoryMock);

		// execute
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		String result = instance.merge(string);

		// assert results
		assertEquals("BaseHibernateRepository.merge did not return expected result", string, result);

		// verify execution
		verify(sessionMock, sessionFactoryMock);
	}

	/**
	 * Test of attachDirty method, of class BaseHibernateRepository.
	 */
	@Test
	public void testFindByExample() {
		String string1 = "String number 1";

		List<String> string1result = new ArrayList<String>();
		string1result.add(string1);

		// create mocks
		Session sessionMock = createMock(Session.class);
		SessionFactory sessionFactoryMock = createMock(SessionFactory.class);
		Criteria criteriaMock = createMock(Criteria.class);

		// set up expectations
		expect(sessionFactoryMock.getCurrentSession()).andReturn(sessionMock);
		expect(sessionMock.createCriteria(String.class.getName())).andReturn(criteriaMock);
		expect(criteriaMock.add(isA(Example.class))).andReturn(criteriaMock);
		expect(criteriaMock.list()).andReturn(string1result);

		// prepare EasyMock
		replay(sessionMock, sessionFactoryMock, criteriaMock);

		// execute
		BaseHibernateRepository<String> instance = new BaseHibernateRepositoryTestClass(sessionFactoryMock);
		instance.findByExample(string1);

		assertEquals(string1result.size(), 1);
		assertEquals(string1result.get(0), string1);

		// verify execution
		verify(sessionMock, sessionFactoryMock, criteriaMock);
	}

}
