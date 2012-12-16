/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2012 Patentstyret
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

import no.mxa.service.batch.confirmation.ReceiptProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmationBatchJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationBatchJob.class);
	private ReceiptProcessor receiptProcessor;

	@Inject
	public ConfirmationBatchJob(ReceiptProcessor receiptProcessor) {
		this.receiptProcessor = receiptProcessor;
	}

	public void execute() {
		LOGGER.debug("start");
		try {
			receiptProcessor.process();
		} catch (Exception e) {
			LOGGER.error("receiptProcessor failed", e);
		}
		LOGGER.debug("stop");
	}

}
