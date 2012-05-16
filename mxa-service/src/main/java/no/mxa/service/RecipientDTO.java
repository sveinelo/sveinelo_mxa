package no.mxa.service;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

/**
 * Class used to send mail recipients to SendMail
 */
public class RecipientDTO {

	private InternetAddress address;

	// Type shall be of value defined in javax.mail.Message.RecipientType.*
	private RecipientType type;

	public RecipientDTO(RecipientType type, InternetAddress address) {
		this.type = type;
		this.address = address;
	}

	/**
	 * @return the recipient
	 */
	public InternetAddress getAddresst() {
		return address;
	}

	/**
	 * @param recipient
	 *            the recipient to set
	 */
	public void setAddress(InternetAddress address) {
		this.address = address;
	}

	/**
	 * @return the type
	 */
	public RecipientType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(RecipientType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return address.getAddress();
	}

}
