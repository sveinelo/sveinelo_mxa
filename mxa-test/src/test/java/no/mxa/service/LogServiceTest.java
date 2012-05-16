package no.mxa.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.dto.LogDTO;
import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class LogServiceTest extends SpringBasedTest {

	@Inject
	LogService logService;
	@Inject
	DataSource dataSource;

	@Before
	public void setUp() {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String withoutReferenceQuery = "INSERT INTO LOG (ID, TIME, LOGTYPE, LOGMSG) VALUES (?, ?, ?, ?)";
		template.update(withoutReferenceQuery, 1, new Date(), "No ref", "No message reference");

		String withReferenceQuery = "INSERT INTO LOG (ID, MESSAGEID, TIME, LOGTYPE, LOGMSG) VALUES (?, ?, ?, ?, ?)";
		template.update(withReferenceQuery, 2, 1, new Date(), "Ref", "Message reference");
	}

	@Test
	public void shouldReturnLogEntryWithoutMessageReference() {
		List<LogDTO> logList = logService.getAllLogsWithNullInMessageId();
		assertThat(logList.size(), is(1));
		assertThat(logList.get(0).getLogType(), is("No ref"));
	}

}