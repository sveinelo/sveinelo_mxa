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
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("start");
		try {
			ApplicationContext appContext = getApplicationContext(context, APPLICATION_CONTEXT_KEY);
			ReceiptProcessor receiptProcessor = (ReceiptProcessor) appContext.getBean("receiptProcessor");
			receiptProcessor.process();
		} catch (SchedulerException e) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("Unable to get applicationcontext when initializing Job.", e);
		} finally {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("stop");
		}

	}

}
