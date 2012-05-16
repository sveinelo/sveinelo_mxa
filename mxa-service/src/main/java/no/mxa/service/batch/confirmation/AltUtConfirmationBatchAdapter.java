package no.mxa.service.batch.confirmation;

import java.io.Reader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.mxa.altinn.castor.xsd.AltUtConfirmationBatch;
import no.mxa.altinn.castor.xsd.Confirmation;
import no.mxa.altinn.castor.xsd.Confirmed;
import no.mxa.altinn.castor.xsd.Read;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AltUtConfirmationBatchAdapter extends AbstractReceiptAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AltUtConfirmationBatchAdapter.class);
	private final DateFormat dateFormat = DateFormat.getDateTimeInstance();

	class AltUtConfirmationMessage extends AbstractMessageAdapter {

		private final Confirmation confirmation;

		AltUtConfirmationMessage(Confirmation confirmation) {
			this.confirmation = confirmation;
		}

		@Override
		public boolean isValid() {
			return confirmation.isValid();
		}

		@Override
		public LogEntry getLogEntry() {
			LogEntryBuilder builder = new LogEntryBuilder(getTypeString());
			builder.setMessageReference(getConfirmation().getMessageReference());
			builder.setShortName(getConfirmation().getShortName());
			if (isRead()) {
				Read read = getConfirmation().getRead();

				Date readDateTime = read.getReadDateTime();
				builder.setReadDateTime(dateFormat.format(readDateTime));
			}
			if (isConfirmed()) {
				Confirmed confirmed = getConfirmation().getConfirmed();

				String participantId = confirmed.getParticipantId();
				builder.setParticipantId(participantId);

				Date confirmedDateTime = confirmed.getConfirmedDateTime();
				builder.setConfirmedDateTime(dateFormat.format(confirmedDateTime));

				String confirmedRoleList = confirmed.getConfirmedRoleList();
				builder.setConfirmedRoleList(confirmedRoleList);

				String loginMethod = confirmed.getLoginMethod().value();
				builder.setLoginMethod(loginMethod);
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug(builder.toString());
			return builder.build();
		}

		private Confirmation getConfirmation() {
			return confirmation;
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
		AltUtConfirmationBatch altUtConfirmationBatch = (AltUtConfirmationBatch) Unmarshaller.unmarshal(
				AltUtConfirmationBatch.class, reader);
		List<MessageAdapter> messages = new ArrayList<MessageAdapter>();
		for (Confirmation confirmation : altUtConfirmationBatch.getAltUtConfirmations().getConfirmation()) {
			messages.add(new AltUtConfirmationMessage(confirmation));
		}
		return messages;
	}

}
