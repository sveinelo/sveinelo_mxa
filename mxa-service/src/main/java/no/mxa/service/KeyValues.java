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
package no.mxa.service;

import java.util.List;

import javax.inject.Inject;

import no.mxa.altinn.ws.api.MessageValues;
import no.mxa.dto.KeyValuesDTO;

/**
 * Singleton that holds keyvalues from database. Loaded when application starts.
 */
public class KeyValues implements MessageValues {
	private final KeyValuesService keyValuesService;

	@Inject
	public KeyValues(KeyValuesService keyValuesService) {
		this.keyValuesService = keyValuesService;
	}

	/**
	 * Looks up values for key in database
	 * 
	 * @param key
	 * @return value
	 */
	private KeyValuesDTO findKeyValue(String key) {
		// Get values from database
		List<KeyValuesDTO> keyValues = keyValuesService.searchByProperty("key", key);

		// Throw exception if not found in DB or multiple values in DB.
		if (keyValues != null && keyValues.size() != 1) {
			throw new RuntimeException("Error reading keyvalue " + key + " from database.");
		}
		return keyValues.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getGovOrgan()
	 */
	@Override
	public String getGovOrgan() {
		return findKeyValue("GOVORGAN").getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getLanguageCode()
	 */
	@Override
	public String getLanguageCode() {
		return findKeyValue("LANGUAGECODE").getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getAltinnPassword()
	 */
	@Override
	public String getAltinnPassword() {
		return findKeyValue("ALTINNPASSWORD").getStringValue();
	}

	public String getSmtpHost() {
		return findKeyValue("SMTPHOST").getStringValue();
	}

	public String getSmtpUser() {
		return findKeyValue("SMTPUSER").getStringValue();
	}

	public String getSmtpPassword() {
		return findKeyValue("SMTPPASSWORD").getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getMailFrom()
	 */
	@Override
	public String getMailFrom() {
		return findKeyValue("MAILFROM").getStringValue();
	}

	public String getReceiptFtpServer() {
		return findKeyValue("RECEIPTFTPSERVER").getStringValue();
	}

	public String getReceiptFtpPath() {
		return findKeyValue("RECEIPTFTPPATH").getStringValue();
	}

	public String getReceiptFtpUser() {
		return findKeyValue("RECEIPTFTPUSER").getStringValue();
	}

	public String getReceiptFtpPassword() {
		return findKeyValue("RECEIPTFTPPASSWORD").getStringValue();
	}

	public Integer getReceiptFtpServerPort() {
		// TODO Document this behavior (and remove default port from code?).
		try {
			return findKeyValue("RECEIPTFTPPORT").getNumericValue();
		} catch (RuntimeException re) {
			return Integer.valueOf(21);
		}
	}

	public String getMailToPat() {
		return findKeyValue("MAILTOPAT").getStringValue();
	}

	public String getMailNoticeSubject() {
		return findKeyValue("MAILNOTICESUBJECT").getStringValue();
	}

	public String getMailNoticeContent() {
		return findKeyValue("MAILNOTICECONTENT").getStringValue();
	}

	public String getMailWarnSubject() {
		return findKeyValue("MAILWARNSUBJECT").getStringValue();
	}

	public String getMailWarnContent() {
		return findKeyValue("MAILWARNCONTENT").getStringValue();
	}

	public Integer getMailWarnDays() {
		return findKeyValue("MAILNOTICEDAYS").getNumericValue();
	}

	public Integer getMailNoticeDays() {
		return findKeyValue("MAILWARNDAYS").getNumericValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getNotificationType()
	 */
	@Override
	public String getNotificationType() {
		return findKeyValue("NOTIFICATIONTYPE").getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getServiceCode()
	 */
	@Override
	public String getServiceCode() {
		return findKeyValue("SERVICECODE").getStringValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.mxa.service.MessageValues#getServiceEdition()
	 */
	@Override
	public String getServiceEdition() {
		return findKeyValue("SERVICEEDITION").getStringValue();
	}

	public Integer getSmtpPort() {
		// TODO Document this behavior (and remove default port from code?).
		try {
			return findKeyValue("SMTPPORT").getNumericValue();
		} catch (RuntimeException re) {
			return Integer.valueOf(25);
		}
	}

}
