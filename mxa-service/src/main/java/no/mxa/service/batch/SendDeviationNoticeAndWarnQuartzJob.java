package no.mxa.service.batch;

import no.mxa.service.batch.notification.SendNoticesAndWarnings;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class SendDeviationNoticeAndWarnQuartzJob extends ApplicationContextQuartzJobBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendDeviationNoticeAndWarnQuartzJob.class);
	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			ApplicationContext appContext = getApplicationContext(context, APPLICATION_CONTEXT_KEY);
			SendNoticesAndWarnings sendNoticesAndWarnings = (SendNoticesAndWarnings) appContext
					.getBean("sendNoticesAndWarnings");
			sendNoticesAndWarnings.start();
		} catch (JobExecutionException e) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("Unable to get applicationcontext when initializing Job.", e);
		} catch (Exception e1) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("Unable to execute job.", e1);
			return;
		}
	}

}
