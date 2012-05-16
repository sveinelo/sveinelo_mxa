package no.mxa.service.batch.notification;

import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;
import no.mxa.service.SendNoticeOrWarningService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendNoticesAndWarningsImpl implements SendNoticesAndWarnings {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticesAndWarningsImpl.class);

	private MessageService messageService;
	private SendNoticeOrWarningService noticeOrWarningService;

	@Inject
	public SendNoticesAndWarningsImpl(MessageService messageService, SendNoticeOrWarningService noticeOrWarningService) {
		this.messageService = messageService;
		this.noticeOrWarningService = noticeOrWarningService;
	}

	@Override
	public void start() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("start");

		for (MessageDTO message : messageService.searchNoticeMessages()) {
			noticeOrWarningService.sendNoticeMail(message);
		}

		for (MessageDTO message : messageService.searchWarnMessages()) {
			noticeOrWarningService.sendWarnMail(message);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("stop");
	}
}
