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
