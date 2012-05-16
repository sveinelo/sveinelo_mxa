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

		assertThat(keyValues.getGovOrgan(), is("TestOrgan"));
		assertThat(keyValues.getShortname(), is("SN"));
		assertThat(keyValues.getViewFormat(), is("Format"));
		assertThat(keyValues.getAllowDelete(), is(1));
		assertThat(keyValues.getPersistent(), is(2));
		assertThat(keyValues.getRoleReqRead(), is("RoleReqRead"));
		assertThat(keyValues.getRoleReqDeleteConfirm(), is("RoleReqDeleteConfirm"));
		assertThat(keyValues.getRoleReqGovAgency(), is("RoleReqGovAgency"));
		assertThat(keyValues.getSender(), is("Sender"));
		assertThat(keyValues.getLoginSecurityLevel(), is("SecLevel"));
		assertThat(keyValues.getRequireConfirmation(), is(4));
		assertThat(keyValues.getAllowUserDeleteDays(), is(7));
		assertThat(keyValues.getLanguageCode(), is("NO"));
		assertThat(keyValues.getNotificationTypeName(), is("NotificationTypeName"));
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
		template.update(keyValuesQuery, 1, "SHORTNAME", null, "SN");
		template.update(keyValuesQuery, 1, "VIEWFORMAT", null, "Format");
		template.update(keyValuesQuery, 1, "ALLOWDELETE", 1, null);
		template.update(keyValuesQuery, 1, "PERSISTENT", 2, null);
		template.update(keyValuesQuery, 1, "ROLEREQREAD", null, "RoleReqRead");
		template.update(keyValuesQuery, 1, "ROLEREQDELETECONFIRM", null, "RoleReqDeleteConfirm");
		template.update(keyValuesQuery, 1, "ROLEREQGOVAGENCY", null, "RoleReqGovAgency");
		template.update(keyValuesQuery, 1, "SENDER", null, "Sender");
		template.update(keyValuesQuery, 1, "LOGINSECURITYLEVEL", null, "SecLevel");
		template.update(keyValuesQuery, 1, "REQUIRECONFIRMATION", 4, null);
		template.update(keyValuesQuery, 1, "ALLOWUSERDELETEDAYS", 7, null);
		template.update(keyValuesQuery, 1, "LANGUAGECODE", null, "NO");
		template.update(keyValuesQuery, 1, "NOTIFICATIONTYPENAME", null, "NotificationTypeName");
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
