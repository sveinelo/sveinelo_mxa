package no.mxa.service.altut;

import static no.mxa.UniversalConstants.MSG_FAILED_ALTINN;
import static no.mxa.UniversalConstants.MSG_SENTALTINN_FALSE;
import static no.mxa.UniversalConstants.MSG_SENTALTINN_TRUE;
import static no.mxa.UniversalConstants.MSG_SENT_ALTINN;
import static no.mxa.UniversalConstants.MSG_STATUS_SEND_ALTINN_FAILED;
import static no.mxa.UniversalConstants.MSG_STATUS_SENT_ALTINN;

import java.net.MalformedURLException;
import java.sql.Timestamp;

import javax.inject.Inject;
import javax.xml.bind.JAXBElement;

import no.mxa.altinn.ws.AltinnFault;
import no.mxa.altinn.ws.ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage;
import no.mxa.altinn.ws.ReceiptExternal;
import no.mxa.altinn.ws.ReceiptStatusEnum;
import no.mxa.altinn.ws.api.AltinnWS;
import no.mxa.altinn.ws.api.CorrespondenceBuilderException;
import no.mxa.dto.LogDTO;
import no.mxa.dto.MessageDTO;
import no.mxa.service.LogGenerator;
import no.mxa.service.LogService;
import no.mxa.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class SendSingleCorrespondenceMessage implements SingleMessageSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendSingleCorrespondenceMessage.class);

	private AltinnWS altinnWS;
	private MessageService messageService;
	private LogGenerator logGenerator;
	private LogService logService;

	@Inject
	public SendSingleCorrespondenceMessage(AltinnWS altinnWS, MessageService messageService, LogGenerator logGenerator,
			LogService logService) {
		this.altinnWS = altinnWS;
		this.messageService = messageService;
		this.logGenerator = logGenerator;
		this.logService = logService;
	}

	@Override
	@Transactional
	public boolean sendMessage(MessageDTO messageDTO) {
		try {
			ReceiptExternal receipt = altinnWS.sendMessage(messageDTO);

			if (ReceiptStatusEnum.OK.equals(receipt.getReceiptStatusCode())) {
				Timestamp time = markMessageAsSentAltinn(messageDTO);
				logSuccess(messageDTO, receipt, time);
				return true;
			} else {
				markMessageAsFailed(messageDTO);
				logFailure(messageDTO, receipt);
			}
		} catch (ICorrespondenceAgencyExternalBasicInsertCorrespondenceBasicV2AltinnFaultFaultFaultMessage e) {
			markMessageAsFailed(messageDTO);
			String altinnFault = createMessageFromAltinnFault(e.getFaultInfo());
			loggExceptionFailure(messageDTO, altinnFault);
		} catch (MalformedURLException | CorrespondenceBuilderException e) {
			markMessageAsFailed(messageDTO);
			loggExceptionFailure(messageDTO, e.getMessage());
		}

		return false;
	}

	private Timestamp markMessageAsSentAltinn(MessageDTO messageDTO) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		MessageDTO message = messageService.searchById(messageDTO.getId());
		message.setSentAltinn(MSG_SENTALTINN_TRUE);
		message.setMessageStatus(MSG_STATUS_SENT_ALTINN);
		message.setSentAltinnDate(time);
		messageService.mergeMessage(message);
		return time;
	}

	private void logSuccess(MessageDTO messageDTO, ReceiptExternal receipt, Timestamp time) {
		String additionalLogMessage = extractRecieptText(receipt);
		LogDTO logEntry = logGenerator.generateLog(additionalLogMessage, MSG_SENT_ALTINN, messageDTO.getId(), time);
		logService.saveLog(logEntry);
	}

	private String extractRecieptText(ReceiptExternal receipt) {
		if (receipt != null && receipt.getReceiptText() != null) {
			return receipt.getReceiptText().getValue();
		}
		return "Unable to extract reciept Text";
	}

	private void loggExceptionFailure(MessageDTO messageDTO, String error) {
		LogDTO logEntry = logGenerator.generateLog(error, MSG_FAILED_ALTINN, messageDTO.getId());
		logService.saveLog(logEntry);
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Sending til Altinn feilet:" + messageDTO.getId() + ":" + error);
		}
	}

	private void markMessageAsFailed(MessageDTO messageDTO) {
		MessageDTO message = messageService.searchById(messageDTO.getId());
		message.setSentAltinn(MSG_SENTALTINN_FALSE);
		message.setMessageStatus(MSG_STATUS_SEND_ALTINN_FAILED);
		messageService.mergeMessage(message);
	}

	private void logFailure(MessageDTO messageDTO, ReceiptExternal receipt) {
		LogDTO logEntry = logGenerator.generateLog(extractRecieptText(receipt), MSG_FAILED_ALTINN, messageDTO.getId());
		logService.saveLog(logEntry);
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Sending til Altinn feilet:" + messageDTO.getId() + ":" + extractRecieptText(receipt));
		}
	}

	private String createMessageFromAltinnFault(AltinnFault altinnFault) {
		StringBuilder sb = new StringBuilder("altinnFault:");

		sb.append(altinnFault.getErrorID());
		sb.append(extractNameAndValue(altinnFault.getAltinnErrorMessage()));
		sb.append(extractNameAndValue(altinnFault.getAltinnExtendedErrorMessage()));
		sb.append(extractNameAndValue(altinnFault.getAltinnLocalizedErrorMessage()));
		sb.append(extractNameAndValue(altinnFault.getErrorGuid()));
		sb.append(extractNameAndValue(altinnFault.getUserGuid()));
		sb.append(extractNameAndValue(altinnFault.getUserId()));

		return sb.toString();
	}

	private String extractNameAndValue(JAXBElement<String> jaxbElement) {
		StringBuilder sb = new StringBuilder(" ");
		sb.append(jaxbElement.getName().getLocalPart());
		sb.append(":'");
		sb.append(jaxbElement.getValue());
		sb.append("'");
		return sb.toString();
	}
}
