package no.mxa.service.implementations;

import java.util.List;

import javax.inject.Inject;

import no.mxa.dataaccess.LogRepository;
import no.mxa.dto.LogDTO;
import no.mxa.service.LogService;

import org.springframework.transaction.annotation.Transactional;

public class LogServiceImpl implements LogService {

	private LogRepository logRepository;

	@Inject
	public LogServiceImpl(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	@Transactional
	@Override
	public LogDTO searchById(Long id) {
		return this.logRepository.findById(id);
	}

	@Transactional
	@Override
	public List<LogDTO> searchByProperty(String propertyName, Object value) {
		return this.logRepository.findByProperty(propertyName, value);
	}

	@Transactional
	@Override
	public void saveLog(LogDTO log) {
		this.logRepository.save(log);
	}

	@Transactional
	@Override
	public List<LogDTO> getAllLogsWithNullInMessageId() {
		return logRepository.findAllLogsWithNullInMessageId();
	}
}