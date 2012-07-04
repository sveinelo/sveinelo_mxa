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
package no.mxa.service.batch.confirmation;

import java.io.Reader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.mxa.altinn.castor.confirmations2010.Confirmation;
import no.mxa.altinn.castor.confirmations2010.Confirmed;
import no.mxa.altinn.castor.confirmations2010.CorrespondenceConfirmations;
import no.mxa.altinn.castor.confirmations2010.Read;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrespondenceConfirmation2010Adapter extends AbstractReceiptAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(CorrespondenceConfirmation2010Adapter.class);

	private final DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();

	class ConfirmationAdapterMessage extends AbstractMessageAdapter {

		private final Confirmation confirmation;

		ConfirmationAdapterMessage(Confirmation confirmation) {
			this.confirmation = confirmation;
		}

		@Override
		public LogEntry getLogEntry() {
			LogEntryBuilder logBuilder = new LogEntryBuilder(getTypeString());
			logBuilder.setMessageReference(getConfirmation().getCorrespondenceReference());
			logBuilder.setShortName(getConfirmation().getServiceCode() + "-" + getConfirmation().getServiceEdition());
			logBuilder.setReportee(getConfirmation().getReportee());
			if (isRead()) {
				Read read = getConfirmation().getRead();
				Date readDateTime = read.getReadDateTime();
				logBuilder.setReadDateTime(dateTimeFormat.format(readDateTime));

			}

			if (isConfirmed()) {
				Confirmed confirmed = getConfirmation().getConfirmed();

				String confirmedRoleList = confirmed.getConfirmedRoleList();
				logBuilder.setConfirmedRoleList(confirmedRoleList);

				Date confirmedDateTime = confirmed.getConfirmedDateTime();
				logBuilder.setConfirmedDateTime(dateTimeFormat.format(confirmedDateTime));

				String userSSN = confirmed.getUserSSN();
				logBuilder.setParticipantId(userSSN);

				int authenticationMethod = confirmed.getAuthenticationMethod();
				logBuilder.setLoginMethod(Integer.valueOf(authenticationMethod).toString());

			}
			LOGGER.debug("{}", logBuilder);
			return logBuilder.build();
		}

		private Confirmation getConfirmation() {
			return confirmation;
		}

		@Override
		public boolean isValid() {
			return confirmation.isValid();
		}

		@Override
		public boolean isConfirmed() {
			return confirmation.getConfirmed() != null;
		}

		@Override
		public boolean isRead() {
			return confirmation.getRead() != null;
		}
	}

	@Override
	List<MessageAdapter> unmarshal(Reader reader) throws MarshalException, ValidationException {
		// Unmarshal the CorrespondenceConfirmations object
		CorrespondenceConfirmations correspondenceConfirmations = (CorrespondenceConfirmations) Unmarshaller.unmarshal(
				CorrespondenceConfirmations.class, reader);

		// Get all Confirmations and put them in Message objects
		List<MessageAdapter> messages = new ArrayList<MessageAdapter>();
		for (Confirmation confirmation : correspondenceConfirmations.getConfirmation()) {
			messages.add(new ConfirmationAdapterMessage(confirmation));
		}
		return messages;
	}

}
