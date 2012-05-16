package no.mxa.web.model;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import no.mxa.dto.MessageDTO;
import no.mxa.service.MessageService;

/**
 * Provides a list of all messages with deviations.
 */
public class DeviationMessagesModel {
	private MessageService messageService;
	private List<MessageDTO> deviationList;

	@Inject
	public DeviationMessagesModel(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Populates the deviationList
	 */
	@PostConstruct
	public void updateDeviationList() {
		deviationList = messageService.searchByMessageDeviations(new Date());
	}

	public List<MessageDTO> getDeviationList() {
		return deviationList;
	}

	public void setDeviationList(List<MessageDTO> deviationList) {
		this.deviationList = deviationList;
	}

}
