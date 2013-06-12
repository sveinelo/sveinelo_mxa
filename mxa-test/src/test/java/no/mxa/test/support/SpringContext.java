/*
 * #%L
 * Integration tests
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
package no.mxa.test.support;

public final class SpringContext {
	private static final String CTX_PREFIX = "classpath*:META-INF/spring";

	static final String CTX_REPOSITORY = CTX_PREFIX + "/repository-config.xml";
	static final String CTX_ALTINN_WS = CTX_PREFIX + "/altinn-ws-context.xml";
	static final String CTX_SERVICE = CTX_PREFIX + "/service-config.xml";
	static final String CTX_SERVICE_TASK = CTX_PREFIX + "/service-task-config.xml";
	static final String CTX_WAR = CTX_PREFIX + "/war-config.xml";

	static final String CTX_TEST_COMMON = CTX_PREFIX + "/test-common-config.xml";
	static final String CTX_TEST_HSQL = CTX_PREFIX + "/test-hsql-config.xml";
	static final String CTX_TEST_ORACLE = CTX_PREFIX + "/test-oracle-config.xml";
}
