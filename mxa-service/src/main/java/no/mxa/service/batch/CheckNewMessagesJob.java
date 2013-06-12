/*
 * #%L
 * Service
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
package no.mxa.service.batch;

import javax.inject.Inject;

import no.mxa.service.altut.MessageSender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that checks for new messages in the database on a regular basis.
 */
public class CheckNewMessagesJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckNewMessagesJob.class);;
	private MessageSender messageSender;

	@Inject
	public CheckNewMessagesJob(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void execute() {
		LOGGER.debug("start");
		try {
			String result = messageSender.sendMessages();
			LOGGER.info(result);
		} catch (Exception e) {
			LOGGER.error("messageSender failed", e);
		}
		LOGGER.debug("stop");
	}

}
