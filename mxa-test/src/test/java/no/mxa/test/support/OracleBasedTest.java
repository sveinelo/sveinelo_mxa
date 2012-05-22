/*
 * #%L
 * Integration tests
 * %%
 * Copyright (C) 2009 - 2012 Patentstyret
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
