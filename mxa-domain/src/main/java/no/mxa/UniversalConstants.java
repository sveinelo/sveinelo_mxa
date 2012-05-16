package no.mxa;

/**
 * This class is final and includes final static fields for use throughout the application.
 * 
 * IMPORTANT: message status. New status has to be greater than existing statuses Remember to update the HashMap in
 * <code>no.mxa.web.converter.MessageStatusConverter</code> which resides in mxa-war layer, when adding new statuses
 * <b>MSG_STATUS_*</b>.
 */
public final class UniversalConstants {
	private UniversalConstants() {
		// Should never be instantiated
	}

	// Constants used in message.sentaltinn
	public static final int MSG_SENTALTINN_FALSE = 0;
	public static final int MSG_SENTALTINN_TRUE = 1;

	// Constants used in message.sentaltinn
	public static final int MSG_OVERDUENOTICE_FALSE = 0;
	public static final int MSG_OVERDUENOTICE_TRUE = 1;

	// Constants used in message.messageStatus
	// IMPORTANT: New status has to be greater than existing statuses
	// Remember to update the HashMap in no.mxa.web.converter.MessageStatusConverter,
	// and filtrerMeldingerAlle.xhtml in <h:selectOneMenu> element add item <f:selectItem>
	// when adding new statuses.
	public static final int MSG_STATUS_RECEIVED = 10;
	public static final String GUI_MSG_STATUS_RECEIVED = "Melding mottatt";
	public static final int MSG_STATUS_SENT_ALTINN = 20;
	public static final String GUI_MSG_STATUS_SENT_ALTINN = "Melding sendt til Altinn";
	public static final int MSG_STATUS_SEND_ALTINN_FAILED = 30;
	public static final String GUI_MSG_STATUS_SEND_ALTINN_FAILED = "Sending til Altinn feilet";
	public static final int MSG_STATUS_READ_IN_ALTINN = 40;
	public static final String GUI_MSG_STATUS_READ_IN_ALTINN = "Melding lest i Altinn";
	public static final int MSG_STATUS_CONFIRMED_IN_ALTINN = 50; // Unused since Altinn do not support status confirmed in
																	// combination with 'Fagsystemintegrasjon'
	public static final String GUI_MSG_STATUS_CONFIRMED_IN_ALTINN = "Melding bekreftet i Altinn";
	public static final int MSG_STATUS_OVERDUEWARN_SENT_TO_AGENCY = 60;
	public static final String GUI_MSG_STATUS_OVERDUEWARN_SENT_TO_AGENCY = "Sak med vedlegg sendt til etat";
	public static final int MSG_STATUS_MANUALLY_REMOVED = 70;
	public static final String GUI_MSG_STATUS_MANUALLY_REMOVED = "Sak fjernet fra MXA manuelt";

	// Constants used in log.logtype
	public enum LogType {
		MSG_READ, MSG_CONFIRMED
	}

	public static final String MSG_PARSE_ERROR = "MSG_PARSE_ERROR";
	public static final String MSG_SAVE_ERROR = "MSG_SAVE_ERROR";
	public static final String MSG_RECEIVED_MXA = "MSG_RECEIVED_MXA";
	public static final String MSG_SENT_ALTINN = "MSG_SENT_ALTINN";
	public static final String MSG_FAILED_ALTINN = "MSG_FAILED_ALTINN";
	public static final String MSG_READ = "MSG_READ";
	public static final String MSG_CONFIRMED = "MSG_CONFIRMED";
	public static final String MSG_ALTINN_RESEND = "MSG_ALTINN_RESEND";
	public static final String MSG_MANUALLY_REMOVED = "MSG_MANUALLY_REMOVED_FROM_DEVIATION_LIST";
	public static final String RCT_PROCESS_FILE = "RCT_PROCESS_FILE";
	public static final String RCT_PROCESS_FILE_FAILED = "RCT_PROCESS_FILE_FAILED";
	public static final String RCT_FTP_FAULT = "RCT_FTP_FAULT";
	public static final String RCT_PROCESS_NO_MSG_REF = "RCT_PROCESS_NO_MSG_REF";
	public static final String LOG_MAIL_OVERDUENOTICE_SENT = "LOG_MAIL_OVERDUENOTICE_SENT";
	public static final String LOG_MAIL_OVERDUEWARN_SENT_TO_AGENCY = "MESSAGE_SENT_TO_AGENCY";
	public static final String LOG_MAIL_OVERDUEWARN_SENT_TO_AGENCY_FROM_GUI = "MESSAGE_SENT_TO_AGENCY_FROM_GUI";
	public static final String MAIL_FAILED_OVERDUENOTICE = "MAIL_FAILED_OVERDUENOTICE";
	public static final String MAIL_FAILED_OVERDUEWARN = "MAIL_FAILED_OVERDUEWARN";

	// Constants related to fixed log messages
	public static final String MSG_PARSE_ERROR_DESCRIPTION = "Parsefeil i meldingsmottak";
	public static final String MSG_SAVE_ERROR_DESCRIPTION = "Lagring av melding feilet";
	public static final String MESSAGE_RECEIVED_MXA_DESCRIPTION = "Melding mottatt i MXA";
	public static final String MESSAGE_ALTINN_RESEND_DESCRIPTION = "Melding merket for resending til Altinn";

	// Constants related to message wrapping
	public static final String CONTACTINFOTYPE_EMAIL = "EMAIL";
	public static final String CONTACTINFOTYPE_SMS = "SMS";

	// Constants related to GUI. These two constants represents the two different deviations a message can have.
	public static final String GUI_DEVIATION_OVERDUE_READ_DEADLINE = "Melding ikke lest innen lesefrist.";
	public static final String GUI_DEVIATION_SEND_ALTINN_FAILED = "Sending til Altinn feilet.";

	// Constants related to e-mail message text
	// PLCHLDR=PLACEHOLDER, the string is a regular expression used to replace text in the database with values from a message.
	public static final String MAIL_PLCHLDR_MESSAGESUMMARY = "\\$\\{messageSummary\\}";
	public static final String MAIL_PLCHLDR_MESSAGEHEADER = "\\$\\{messageHeader\\}";
	public static final String MAIL_PLCHLDR_CASEDESCRIPTION = "\\$\\{caseDescription\\}";

	// Constants related to web service return codes
	public static final int WEB_SERVICE_MESSAGE_SAVED = 0;
	public static final int WEB_SERVICE_MESSAGE_SAVING_ERROR = 1;
	public static final int WEB_SERVICE_MESSAGE_VALIDATION_ERROR = 2;

	// Character set
	public static final String CHARACTER_SET_UTF8 = "UTF-8";

}
