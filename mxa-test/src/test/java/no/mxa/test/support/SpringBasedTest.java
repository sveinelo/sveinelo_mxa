package no.mxa.test.support;

import static no.mxa.test.support.SpringContext.CTX_ALTINN_WS;
import static no.mxa.test.support.SpringContext.CTX_REPOSITORY;
import static no.mxa.test.support.SpringContext.CTX_SERVICE;
import static no.mxa.test.support.SpringContext.CTX_TEST_COMMON;
import static no.mxa.test.support.SpringContext.CTX_TEST_HSQL;
import static no.mxa.test.support.SpringContext.CTX_WAR;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { CTX_WAR, CTX_ALTINN_WS, CTX_SERVICE, CTX_REPOSITORY, CTX_TEST_COMMON, CTX_TEST_HSQL })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class SpringBasedTest {
	private static final String DELETE_FROM_KEYVALUES = "DELETE FROM KEYVALUES";
	private static final String DELETE_FROM_MESSAGE = "DELETE FROM MESSAGE";
	private static final String DELETE_FROM_LOG = "DELETE FROM LOG";
	private static final String DELETE_FROM_CONTACTINFO = "DELETE FROM CONTACTINFO";
	private static final String DELETE_FROM_ATTACHMENT = "DELETE FROM ATTACHMENT";
	@Inject
	private DataSource dataSource;
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Before
	public void prepareDataBase() {
		simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	/**
	 * Does not clear Quartz tables.
	 */
	protected void emptyAllDomainTables() {
		simpleJdbcTemplate.update(DELETE_FROM_KEYVALUES);
		simpleJdbcTemplate.update(DELETE_FROM_MESSAGE);
		simpleJdbcTemplate.update(DELETE_FROM_LOG);
		simpleJdbcTemplate.update(DELETE_FROM_CONTACTINFO);
		simpleJdbcTemplate.update(DELETE_FROM_ATTACHMENT);
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

}
