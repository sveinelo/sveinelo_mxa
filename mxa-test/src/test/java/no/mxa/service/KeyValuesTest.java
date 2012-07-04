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
package no.mxa.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.inject.Inject;
import javax.sql.DataSource;

import no.mxa.test.support.SpringBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class KeyValuesTest extends SpringBasedTest {

	@Inject
	KeyValues keyValues;
	@Inject
	DataSource dataSource;
	private SimpleJdbcTemplate template;

	@Before
	public void setUp() {
		template = new SimpleJdbcTemplate(dataSource);
	}

	@Test
	public void shouldReturnCorrectKeyValues() {
		prepareTestData();

		assertThat(keyValues.getLanguageCode(), is("NO"));
		assertThat(keyValues.getAltinnPassword(), is("AltinnPassword"));
		assertThat(keyValues.getSmtpHost(), is("SmtpHost"));
		assertThat(keyValues.getSmtpUser(), is("SmtpUser"));
		assertThat(keyValues.getSmtpPassword(), is("SmtpPassword"));
		assertThat(keyValues.getMailFrom(), is("MailFrom"));
		assertThat(keyValues.getReceiptFtpServer(), is("ReceiptFtpServer"));
		assertThat(keyValues.getReceiptFtpPath(), is("ReceiptFtpPath"));
		assertThat(keyValues.getReceiptFtpUser(), is("ReceiptFtpUser"));
		assertThat(keyValues.getReceiptFtpPassword(), is("ReceiptFtpPassword"));
		assertThat(keyValues.getMailToPat(), is("MailToPat"));
		assertThat(keyValues.getMailNoticeSubject(), is("MailNoticeSubject"));
		assertThat(keyValues.getMailNoticeContent(), is("MailNoticeContent"));
		assertThat(keyValues.getMailWarnSubject(), is("MailWarnSubject"));
		assertThat(keyValues.getMailWarnContent(), is("MailWarnContent"));
	}

	private void prepareTestData() {
		String keyValuesQuery = "INSERT INTO KEYVALUES " + "(ID, KEY_NAME, NUMERICVALUE, STRINGVALUE) " + "VALUES (?, ?, ?, ?)";
		template.update(keyValuesQuery, 1, "GOVORGAN", null, "TestOrgan");
		template.update(keyValuesQuery, 1, "LANGUAGECODE", null, "NO");
		template.update(keyValuesQuery, 1, "ALTINNPASSWORD", null, "AltinnPassword");
		template.update(keyValuesQuery, 1, "SMTPHOST", null, "SmtpHost");
		template.update(keyValuesQuery, 1, "SMTPUSER", null, "SmtpUser");
		template.update(keyValuesQuery, 1, "SMTPPASSWORD", null, "SmtpPassword");
		template.update(keyValuesQuery, 1, "MAILFROM", null, "MailFrom");
		template.update(keyValuesQuery, 1, "RECEIPTFTPSERVER", null, "ReceiptFtpServer");
		template.update(keyValuesQuery, 1, "RECEIPTFTPPATH", null, "ReceiptFtpPath");
		template.update(keyValuesQuery, 1, "RECEIPTFTPUSER", null, "ReceiptFtpUser");
		template.update(keyValuesQuery, 1, "RECEIPTFTPPASSWORD", null, "ReceiptFtpPassword");
		template.update(keyValuesQuery, 1, "MAILTOPAT", null, "MailToPat");
		template.update(keyValuesQuery, 1, "MAILNOTICESUBJECT", null, "MailNoticeSubject");
		template.update(keyValuesQuery, 1, "MAILNOTICECONTENT", null, "MailNoticeContent");
		template.update(keyValuesQuery, 1, "MAILWARNSUBJECT", null, "MailWarnSubject");
		template.update(keyValuesQuery, 1, "MAILWARNCONTENT", null, "MailWarnContent");
	}

}
