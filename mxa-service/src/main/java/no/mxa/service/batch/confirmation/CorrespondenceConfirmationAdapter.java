package no.mxa.service.batch.confirmation;

import java.io.Reader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.mxa.altinn.castor.confirmations.Confirmation;
import no.mxa.altinn.castor.confirmations.Confirmed;
import no.mxa.altinn.castor.confirmations.CorrespondenceConfirmations;
import no.mxa.altinn.castor.confirmations.Read;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrespondenceConfirmationAdapter extends AbstractReceiptAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(CorrespondenceConfirmationAdapter.class);

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
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(logBuilder.toString());
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
