package no.mxa.service.implementations;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import no.mxa.UniversalConstants;
import no.mxa.dataaccess.MessageRepository;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.MessageService;

import org.springframework.transaction.annotation.Transactional;

public class MessageServiceImpl implements MessageService {

	@Inject
	private MessageRepository messageRepository;
	@Inject
	// TODO: There is a circular reference between LogGenerator and MessageService
	private LogGenerator logGenerator;

	@Override
	@Transactional
	public MessageDTO searchById(Long id) {
		return this.messageRepository.findById(id);
	}

	@Override
	@Transactional
	public List<MessageDTO> searchByProperty(String propertyName, Object value) {
		return this.messageRepository.findByProperty(propertyName, value);
	}

	@Override
	@Transactional
	public void saveMessage(MessageDTO message) {
		this.messageRepository.save(message);
	}

	@Override
	@Transactional
	public void mergeMessage(MessageDTO message) {
		this.messageRepository.merge(message);
	}

	@Override
	@Transactional
	public List<MessageDTO> searchByCriteria(MessageDTO criteria) {
		return this.messageRepository.findByExample(criteria);
	}

	@Override
	@Transactional
	public List<MessageDTO> searchByMessageDeviations(Date presentDate) {
		return this.messageRepository.findDeviations(presentDate);
	}

	@Override
	@Transactional
	public MessageDTO searchByMessageKey(String messageKey) {
		List<MessageDTO> list = this.messageRepository.findByProperty("messageKey", messageKey);
		if (list.size() != 1) {
			return null;
		} else {
			return list.get(0);

		}
	}

	@Override
	@Transactional
	public void updateMessageStatusAndSentAltinnFlag(long messageId) {
		MessageDTO messageDTOToUpdate = this.messageRepository.findById(messageId);

		int sentAltinn = messageDTOToUpdate.getSentAltinn();

		// Marks message for re-send only if sentAltinn = '1'
		if (sentAltinn == UniversalConstants.MSG_SENTALTINN_TRUE
				|| messageDTOToUpdate.getMessageStatus() == UniversalConstants.MSG_STATUS_SEND_ALTINN_FAILED) {
			// Generate a new log entry
			LogDTO logDTO = this.logGenerator.generateLog(UniversalConstants.MESSAGE_ALTINN_RESEND_DESCRIPTION,
					UniversalConstants.MSG_ALTINN_RESEND, messageId);
			logGenerator.saveLog(logDTO);

			// Update sentAltinn flag to '0' and messageStatus to '10'
			// Attach new log entry to the message
			messageDTOToUpdate.setSentAltinn(0);
			messageDTOToUpdate.setMessageStatus(UniversalConstants.MSG_STATUS_RECEIVED);
			messageDTOToUpdate.setSentAltinnDate(null);
			messageDTOToUpdate.getLog().add(logDTO);
			this.messageRepository.merge(messageDTOToUpdate);
		}
	}

	@Override
	@Transactional
	public void updateMessageStatus(long messageId) {
		MessageDTO messageDTOToUpdate = this.messageRepository.findById(messageId);

		LogDTO logDTO = this.logGenerator.generateLog(UniversalConstants.GUI_MSG_STATUS_MANUALLY_REMOVED,
				UniversalConstants.MSG_MANUALLY_REMOVED, messageId);

		logGenerator.saveLog(logDTO);

		messageDTOToUpdate.setMessageStatus(UniversalConstants.MSG_STATUS_MANUALLY_REMOVED);
		messageDTOToUpdate.getLog().add(logDTO);
		this.messageRepository.merge(messageDTOToUpdate);
	}

	@Override
	@Transactional
	public List<MessageDTO> searchFromGUI(MessageDTO criteria, Date fromDate, Date toDate, String caseDescription,
			String messageReference) {
		return this.messageRepository.findByCriteriaFromGUI(criteria, fromDate, toDate, caseDescription, messageReference);
	}

	@Override
	@Transactional
	public boolean hasDeviation(String messageKey) {
		boolean returnValue = false;
		Date presentDate = new Date();

		List<MessageDTO> deviationList = this.messageRepository.findDeviations(presentDate);

		// Check if input id is in deviation list
		for (int i = 0; i < deviationList.size(); i++) {
			if (messageKey.equals(deviationList.get(i).getMessageKey())) {
				returnValue = true;
				break;
			}
		}

		return returnValue;
	}

	@Override
	@Transactional
	public List<MessageDTO> searchNoticeMessages() {
		return messageRepository.findNoticeMessages(new Date());
	}

	@Override
	@Transactional
	public List<MessageDTO> searchWarnMessages() {
		return messageRepository.findWarnMessages(new Date());
	}
}