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
package no.mxa.service.batch;

import java.io.IOException;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.service.NotUniqueMessageException;
import no.mxa.service.batch.confirmation.ReceiptProcessor;
import no.mxa.test.support.FreePortUtil;
import no.mxa.test.support.SpringBasedTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReceiptProcessorTest extends SpringBasedTest {

	@Inject
	private DataSource dataSource;
	@Inject
	private ReceiptProcessor receiptProcessor;
	private FakeFtpServer fake;

	/**
	 * Initial setup.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		emptyAllDomainTables();
		SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);

		String keyvaluesQuery = "INSERT INTO KEYVALUES (ID, KEY_NAME, DATEVALUE, NUMERICVALUE, STRINGVALUE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?)";
		template.update(keyvaluesQuery, 1L, "RECEIPTFTPSERVER", null, null, "localhost", null);
		template.update(keyvaluesQuery, 2L, "RECEIPTFTPUSER", null, null, "ftp_user", null);
		template.update(keyvaluesQuery, 3L, "RECEIPTFTPPASSWORD", null, null, "passord", null);
		template.update(keyvaluesQuery, 4L, "RECEIPTFTPPATH", null, null, "/altinn/test/kvittering", null);
		int localPort = FreePortUtil.findPort();
		template.update(keyvaluesQuery, 5L, "RECEIPTFTPPORT", null, localPort, null, null);
		fake = new FakeFtpServer();
		fake.setServerControlPort(localPort);
		fake.addUserAccount(new UserAccount("ftp_user", "passord", "/"));
		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new FileEntry("/altinn/test/kvittering/rubbish.log", "Content of test file rubbish.log"));
		fileSystem.add(new DirectoryEntry("/altinn/test/kvittering/processed"));
		fake.setFileSystem(fileSystem);
		fake.start();
	}

	@After
	public void tearDown() {
		fake.stop();
	}

	@Test
	public void testReceiptProcessor() throws IOException, NotUniqueMessageException {
		receiptProcessor.process();
	}

}
