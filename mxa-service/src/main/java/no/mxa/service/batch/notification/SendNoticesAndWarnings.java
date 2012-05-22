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

import javax.mail.MessagingException;

public interface SendNoticesAndWarnings {

	/**
	 * Sends overdue-notices and warnings. * OverdueNotice = readDeadline > today and overdueNotice not sent. Warning =
	 * readDeadline > today and overdueNotice sent.
	 * 
	 * OverdueNotice is sent after seven days, warning is sent after 14 days.
	 * 
	 * Warning is not the best word. It is the complete message with attachments sent to a specified e-mail, from keyValues,
	 * where a case officer can manually take care of the message. (For example send it by snail-mail.)
	 * 
	 * @throws MessagingException
	 */
	void start() throws MessagingException;
}
