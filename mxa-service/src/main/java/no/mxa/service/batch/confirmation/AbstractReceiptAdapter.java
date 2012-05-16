package no.mxa.service.batch.confirmation;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public abstract class AbstractReceiptAdapter implements ReceiptAdapter {

	public AbstractReceiptAdapter() {
		super();
	}

	@Override
	public List<MessageAdapter> parseXml(final String xml) throws MarshalException, ValidationException {
		return unmarshal(new StringReader(xml));
	}

	abstract List<MessageAdapter> unmarshal(Reader reader) throws MarshalException, ValidationException;

}