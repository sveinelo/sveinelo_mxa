package no.mxa.service.batch;

import no.mxa.service.altut.MessageSender;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * Class that checks for new messages in the database on a regular basis.
 */
public class CheckNewMessagesQuartzJob extends ApplicationContextQuartzJobBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckNewMessagesQuartzJob.class);;
	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";
	private MessageSender messageSender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("start");
		// Get the application context beans
		try {
			ApplicationContext appContext = getApplicationContext(context, APPLICATION_CONTEXT_KEY);
			messageSender = (MessageSender) appContext.getBean("messageSender");
		} catch (Exception e1) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("Unable to get applicationcontext when initializing Job. Exception:", e1);
			return;
		}
		// Send all unsent messages to Altinn
		String result = messageSender.sendMessages();
		if (LOGGER.isInfoEnabled())
			LOGGER.info(result);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("stop");
	}

}
