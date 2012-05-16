package no.mxa.service.batch.confirmation;

import no.mxa.UniversalConstants.LogType;

public abstract class AbstractMessageAdapter implements MessageAdapter {

	@Override
	public final LogType getLogType() {
		if (isConfirmed())
			return LogType.MSG_CONFIRMED;
		if (isRead())
			return LogType.MSG_READ;
		return null;
	}

	public String getTypeString() {
		String type;
		if (isConfirmed())
			type = "Confirmed";
		else if (isRead())
			type = "Read";
		else
			type = null;
		return type;
	}

}
