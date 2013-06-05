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

import no.mxa.service.batch.confirmation.ReceiptProcessor;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class ConfirmationBatchQuartzJob extends ApplicationContextQuartzJobBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationBatchQuartzJob.class);
	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOGGER.debug("start");
		try {
			ApplicationContext appContext = getApplicationContext(context, APPLICATION_CONTEXT_KEY);
			ReceiptProcessor receiptProcessor = (ReceiptProcessor) appContext.getBean("receiptProcessor");
			receiptProcessor.process();
		} catch (SchedulerException e) {
			LOGGER.error("Unable to get applicationcontext when initializing Job.", e);
		} finally {
			LOGGER.debug("stop");
		}

	}

}
