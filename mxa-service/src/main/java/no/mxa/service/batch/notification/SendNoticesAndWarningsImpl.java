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
package no.mxa.service.batch.notification;

import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;
import no.mxa.service.SendNoticeOrWarningService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendNoticesAndWarningsImpl implements SendNoticesAndWarnings {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendNoticesAndWarningsImpl.class);

	private final MessageService messageService;
	private final SendNoticeOrWarningService noticeOrWarningService;

	@Inject
	public SendNoticesAndWarningsImpl(MessageService messageService, SendNoticeOrWarningService noticeOrWarningService) {
		this.messageService = messageService;
		this.noticeOrWarningService = noticeOrWarningService;
	}

	@Override
	public void start() {
		LOGGER.debug("start");

		for (MessageDTO message : messageService.searchNoticeMessages()) {
			noticeOrWarningService.sendNoticeMail(message);
		}

		for (MessageDTO message : messageService.searchWarnMessages()) {
			noticeOrWarningService.sendWarnMail(message);
		}

		LOGGER.debug("stop");
	}
}
