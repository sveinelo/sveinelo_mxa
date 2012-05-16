package no.mxa.service.batch;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class ApplicationContextQuartzJobBean extends QuartzJobBean {
	/**
	 * Helper method to get the Spring IoC-container.
	 * 
	 * @param context
	 * @return
	 * @throws SchedulerException
	 */
	ApplicationContext getApplicationContext(JobExecutionContext context, String applicationContextKey)
			throws SchedulerException {
		ApplicationContext appCtx = null;
		appCtx = (ApplicationContext) context.getScheduler().getContext().get(applicationContextKey);
		if (appCtx == null) {
			throw new JobExecutionException("No application context available in scheduler context for key \""
					+ applicationContextKey + "\"");
		}
		return appCtx;
	}

}
