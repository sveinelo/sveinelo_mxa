package no.mxa.altinn.ws.api;

/**
 * These values are required to generate a Correspondence.
 */
public interface MessageValues {

	/**
	 * @return the govOrgan
	 */
	String getGovOrgan();

	/**
	 * @return the languageCode
	 */
	String getLanguageCode();

	/**
	 * @return the altinnPassword
	 */
	String getAltinnPassword();

	/**
	 * @return the mailFrom
	 */
	String getMailFrom();

	/**
	 * @return the notificationType
	 */
	String getNotificationType();

	/**
	 * @return serviceCode
	 */
	String getServiceCode();

	/**
	 * @return serviceEdition
	 */
	String getServiceEdition();

}