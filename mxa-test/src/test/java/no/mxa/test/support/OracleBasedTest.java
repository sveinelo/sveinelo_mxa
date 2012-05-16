package no.mxa.test.support;

import static no.mxa.test.support.SpringContext.CTX_REPOSITORY;
import static no.mxa.test.support.SpringContext.CTX_SERVICE;
import static no.mxa.test.support.SpringContext.CTX_TEST_COMMON;
import static no.mxa.test.support.SpringContext.CTX_TEST_ORACLE;
import static no.mxa.test.support.SpringContext.CTX_WAR;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { CTX_WAR, CTX_SERVICE, CTX_REPOSITORY, CTX_TEST_COMMON, CTX_TEST_ORACLE })
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class OracleBasedTest {

}
