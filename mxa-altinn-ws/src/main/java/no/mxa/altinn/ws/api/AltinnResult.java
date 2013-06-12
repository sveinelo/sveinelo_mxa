/*
 * #%L
 * Altinn Webservice
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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
package no.mxa.altinn.ws.api;

/**
 * Class used for result of parsing result XML from Altinn
 * 
 * 
 */
public class AltinnResult {
	private final String result;
	private final String messageEntry;

	public AltinnResult(String result, String messageEntry) {
		this.result = result;
		this.messageEntry = messageEntry;
	}
	
	public String getResult() {
		return result;
	}

	public String getMessageEntry() {
		return messageEntry;
	}

}
