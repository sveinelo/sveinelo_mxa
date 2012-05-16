package no.mxa.test.support;

public final class SpringContext {
	private static final String CTX_PREFIX = "classpath*:META-INF/spring";

	static final String CTX_REPOSITORY = CTX_PREFIX + "/repository-config.xml";
	static final String CTX_ALTINN_WS = CTX_PREFIX + "/altinn-ws-context.xml";
	static final String CTX_SERVICE = CTX_PREFIX + "/service-config.xml";
	static final String CTX_SERVICE_QUARTZ = CTX_PREFIX + "/service-quartz-config.xml";
	static final String CTX_WAR = CTX_PREFIX + "/war-config.xml";

	static final String CTX_TEST_COMMON = CTX_PREFIX + "/test-common-config.xml";
	static final String CTX_TEST_HSQL = CTX_PREFIX + "/test-hsql-config.xml";
	static final String CTX_TEST_ORACLE = CTX_PREFIX + "/test-oracle-config.xml";
}
