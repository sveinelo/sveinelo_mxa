package no.mxa.service.batch.confirmation.itest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

import no.mxa.service.KeyValues;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.batch.confirmation.BatchFTPLoaderImpl;
import no.mxa.service.batch.confirmation.MxaFtpException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/*
 * #%L
 * Service
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
/*
 * These tests require firewall openings.
 */
@RunWith(MockitoJUnitRunner.class)
public class BatchFTPLoaderImplTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(BatchFTPLoaderImpl.class);
	private static final String FAKE_FTP_HOSTNAME = "localhost";

	@Mock
	private KeyValues keyValues;
	@Mock
	private LogGenerator logGenerator;
	@Mock
	private LogService logService;

	private FakeFtpServer fakeFtpServer;
	private UserAccount userAccount;

	private BatchFTPLoaderImpl batchFTPLoader;

	@Before
	public void createBatchFTPLoader() throws Exception {
		batchFTPLoader = new BatchFTPLoaderImpl(keyValues, logGenerator, logService);
	}

	@Before
	public void setupKeyValues() {
		when(keyValues.getReceiptFtpServer()).thenReturn(FAKE_FTP_HOSTNAME);
		when(keyValues.getReceiptFtpUser()).thenReturn(userAccount.getUsername());
		when(keyValues.getReceiptFtpPassword()).thenReturn(userAccount.getPassword());
		when(keyValues.getReceiptFtpPath()).thenReturn("/data");

	}

	@Before
	public void createFakeFtpServer() throws Exception {
		fakeFtpServer = new FakeFtpServer();
		userAccount = new UserAccount("user", "password", "/data");
		fakeFtpServer.addUserAccount(userAccount);

		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new DirectoryEntry("/data"));
		fileSystem.add(new DirectoryEntry("/data/processed"));
		fileSystem.add(new FileEntry("/data/file1.txt", "abcdef 1234567890"));
		fileSystem.add(new FileEntry("/data/file4.xml", "abcdef 1234567890"));
		fileSystem.add(new FileEntry("/data/run.exe"));
		fileSystem.add(new FileEntry("/data/test.log", "lhzhgflxjg"));
		addRealFiles(fileSystem, "/data/");
		fakeFtpServer.setFileSystem(fileSystem);

		fakeFtpServer.start();
		LOGGER.trace("Fake FTP server started.");
	}

	private void addRealFiles(FileSystem fileSystem, String path) {
		addFileToMap(fileSystem, path, "BOMTest.log");
		addFileToMap(fileSystem, path, "AltUtConfirmationBatchExample.xml");
		addFileToMap(fileSystem, path, "AltUtConfirmationBatchExampleBOM.xml");
		addFileToMap(fileSystem, path, "BrokenAltUtConfirmationBatchExample.xml");
		addFileToMap(fileSystem, path, "CorrespondenceConfirmationsEksempel.xml");
	}

	private void addFileToMap(FileSystem fileSystem, String path, String filename) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
			Resource classPathResource = new ClassPathResource(filename);
			IOUtils.copy(classPathResource.getInputStream(), outputStream);
			FileEntry entry = new FileEntry(path + filename);
			entry.setContents(outputStream.toByteArray());
			fileSystem.add(entry);
		} catch (IOException e) {
			LOGGER.error("Error while moving file to fakeFTP", e);
			fail("Error while copying the file to byte[]");
		}
	}

	@After
	public void removeFakeFtp() {
		fakeFtpServer.stop();
		LOGGER.trace("Fake FTP server stopped.");
	}

	@Test
	public void testGetFilesFromFtpAsString() throws MxaFtpException {
		Map<String, String> results = batchFTPLoader.getFilesFromFtpAsString();
		assertFalse(results.isEmpty());
	}

	@Test
	public void testFtpNotAvailable() {
		fakeFtpServer.stop();
		try {
			batchFTPLoader.getFilesFromFtpAsString();
		} catch (MxaFtpException e) {
			assertThat(e.getCause(), is(ConnectException.class));

		}
	}

	@Test
	public void testProcessEmptyFileList() throws MxaFtpException {
		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new DirectoryEntry("/data"));
		fakeFtpServer.setFileSystem(fileSystem);
		Map<String, String> loadFilesFromFTP = batchFTPLoader.getFilesFromFtpAsString();
		assertTrue("We need to have an empty list if there are no files.", loadFilesFromFTP.isEmpty());
	}

	@Test
	public void testIfRightFilesGetRead() throws MxaFtpException {
		Map<String, String> filesFromFTP = batchFTPLoader.getFilesFromFtpAsString();
		assertEquals("Our Testset of files should contain some files that are not to be processed.", 7, filesFromFTP.size());

		// Check that the files got removed in the old location
		for (String filename : filesFromFTP.keySet()) {
			@SuppressWarnings("rawtypes")
			List dataFileNames = fakeFtpServer.getFileSystem().listNames("/data");
			assertFalse(filename + " should not be in the base folder anymore", dataFileNames.contains(filename));
		}
		// Check that the files are now in the new Location.
		for (String filename : filesFromFTP.keySet()) {
			@SuppressWarnings("rawtypes")
			List dataFileNames = fakeFtpServer.getFileSystem().listNames("/data/" + BatchFTPLoaderImpl.PROCESSED_FOLDER_NAME);
			assertTrue(filename + " should not be in the base folder anymore", dataFileNames.contains(filename));
		}

	}

	@Test
	public void testThatFilesWithBomReadCorrectly() throws MxaFtpException {
		Map<String, String> filesFromFtpAsString = batchFTPLoader.getFilesFromFtpAsString();
		LOGGER.debug(filesFromFtpAsString.keySet().toString());
		String bomTest = filesFromFtpAsString.get("BOMTest.log");
		assertEquals("Dette er en test", bomTest);
	}

}
