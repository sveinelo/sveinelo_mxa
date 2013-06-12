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

import no.mxa.service.batch.notification.SendNoticesAndWarnings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendDeviationNoticeAndWarnJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendDeviationNoticeAndWarnJob.class);
	private SendNoticesAndWarnings sendNoticesAndWarnings;

	@Inject
	public SendDeviationNoticeAndWarnJob(SendNoticesAndWarnings sendNoticesAndWarnings) {
		this.sendNoticesAndWarnings = sendNoticesAndWarnings;
	}

	public void execute() {
		try {
			sendNoticesAndWarnings.start();
		} catch (Exception e) {
			LOGGER.error("Unable to execute job.", e);
		}
	}

}
