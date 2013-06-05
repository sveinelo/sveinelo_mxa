/*
 * #%L
 * Service
 * %%
 * Copyright (C) 2009 - 2013 Patentstyret
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
package no.mxa.service.batch.confirmation;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import no.mxa.utils.UnicodeUtil;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public abstract class AbstractReceiptAdapter implements ReceiptAdapter {

	public AbstractReceiptAdapter() {
		super();
	}

	@Override
	public List<MessageAdapter> parseXml(final String xml) throws MarshalException, ValidationException {
		String decodedXml = UnicodeUtil.decode(xml, "UTF-8");
		return unmarshal(new StringReader(decodedXml));
	}

	abstract List<MessageAdapter> unmarshal(Reader reader) throws MarshalException, ValidationException;

}
