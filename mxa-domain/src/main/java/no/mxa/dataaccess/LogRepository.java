package no.mxa.dataaccess;

import java.util.List;

import no.mxa.dto.LogDTO;
import no.mxa.utils.hibernate.IBaseHibernateRepository;

/**
 * Data access interface for Log.
 */
public interface LogRepository extends IBaseHibernateRepository<LogDTO> {

	List<LogDTO> findAllLogsWithNullInMessageId();

}
