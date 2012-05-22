/*
 * #%L
 * Domain
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
package no.mxa.dataaccess;

import java.util.Date;
import java.util.List;

import no.mxa.dto.MessageDTO;
import no.mxa.utils.hibernate.IBaseHibernateRepository;

/**
 * Data access interface for Message.
 * 
 */
public interface MessageRepository extends IBaseHibernateRepository<MessageDTO> {

	/**
	 * 
	 * @return A list of MessageDTOs that: 1) Have failed in sending OR 2) Have been sent but not read/confirmed within
	 *         readDeadLine
	 */
	List<MessageDTO> findDeviations(Date presentDate);

	/**
	 * 
	 * @param criteria
	 *            The MessageDTO criteria
	 * @param fromDate
	 *            Possible from date, ignored if null
	 * @param toDate
	 *            Possible to date, ignored if null
	 * @param caseDescription
	 *            Possible case description, ignored if null
	 * @param messageReference
	 *            Possible message reference, ignored if null
	 * @return A list of MessageDTOs matching the search criteria
	 */
	List<MessageDTO> findByCriteriaFromGUI(MessageDTO criteria, Date fromDate, Date toDate, String caseDescription,
			String messageReference);

	/**
	 * 
	 * @param presentDate
	 * @return Messages with OVERDUENOTICESENT=0 and READDEADLINE > presentDate.
	 */
	List<MessageDTO> findNoticeMessages(Date presentDate);

	/**
	 * 
	 * @param presentDate
	 * @return Messages with OVERDUENOTICESENT=1 and READDEADLINE > presentDate.
	 */
	List<MessageDTO> findWarnMessages(Date presentDate);
}
