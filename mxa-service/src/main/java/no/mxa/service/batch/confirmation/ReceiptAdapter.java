package no.mxa.service.batch.confirmation;

import java.util.List;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public interface ReceiptAdapter {

	List<MessageAdapter> parseXml(String xml) throws MarshalException, ValidationException;

}
