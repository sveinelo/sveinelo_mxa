package no.mxa.web.model;

import java.text.DateFormat;
import java.util.Date;

/**
 * Provides data to the footer div.
 */
public class FooterModel {
	private String developedBy;
	private String version;

	public FooterModel(String version) {
		developedBy = "Applikasjonen ble sist startet: " + DateFormat.getDateInstance().format(new Date());

		this.version = version;
	}

	public String getDevelopedBy() {
		return developedBy;
	}

	public String getVersion() {
		return version;
	}
}
