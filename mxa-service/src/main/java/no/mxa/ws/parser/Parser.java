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
package no.mxa.ws.parser;

/**
 * Class for validating and parsing message received from Agency
 */
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import no.mxa.UniversalConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Parser extends DefaultHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

	// Class variables
	private String tempVal;
	private String tempQName = "";
	private Message tempMessage;
	private Attachment tempAttachment;
	private ContactInfo tempContactInfo;
	private CharArrayWriter tempFil;
	private List<Attachment> attachments;
	private List<ContactInfo> contactInfo;
	private InputSource document;
	private StringBuilder stringBuilder;
	private final Resource schema;

	public Parser(Resource schema) {
		this.schema = schema;
	}

	/**
	 * 
	 * Validates xml against SantMxa.xsd
	 * 
	 * @param xmlString
	 *            - xml message to be validated
	 * @return - OK if the xml validates, otherwise an error message explaining why the message is invalid
	 */
	public String validateDocument(String xmlString) {
		document = new InputSource(new StringReader(xmlString));
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);

			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

			factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(schema.getInputStream()) }));

			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			MyErrorHandler errorHandler = new MyErrorHandler();
			reader.setErrorHandler(errorHandler);
			reader.parse(document);

			return errorHandler.getReturnMessage();
		} catch (SAXException e) {

			LOGGER.error("Problem validating document.", e);
			return "SAXException: " + e.getMessage();
		} catch (IOException e) {
			LOGGER.error("IOException while validating document.", e);
			return "IOException: " + e.getMessage();
		} catch (ParserConfigurationException e) {
			LOGGER.error("ParserConfigurationException: ", e);
			return "ParerConfigurationException: " + e.getMessage();
		}
	}

	/**
	 * 
	 * Class for handling errors that can occur while validating xml. Returns error category and explanation of what went wrong
	 * 
	 */
	private class MyErrorHandler extends DefaultHandler {

		private String returnMessage = "OK";

		@Override
		public void warning(SAXParseException e) throws SAXException {
			returnMessage = "Warning: " + e.getMessage();
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			returnMessage = "Error: " + e.getMessage();
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
			returnMessage = "Fatal error: " + e.getMessage();
		}

		public String getReturnMessage() {
			return returnMessage;
		}

	}

	/**
	 * 
	 * Parses the xml. This method should not be called if the document is invalid after calling validateDocument
	 * 
	 * @param xmlString
	 *            - xml message to be parsed
	 * @returns - a Message object based on xml elements, or null if the parsing fails for some reason
	 */
	public Message parseDocument(String xmlString) {
		attachments = new ArrayList<Attachment>();
		contactInfo = new ArrayList<ContactInfo>();
		document = new InputSource(new StringReader(xmlString));

		// get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			// get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			// parse the file and also register this class for call backs
			sp.parse(document, this);
			return tempMessage;
		} catch (SAXException e) {
			LOGGER.error("Error parsing document.", e);
			return null;
		} catch (IOException e) {
			LOGGER.error("Error parsing document.", e);
			return null;
		} catch (ParserConfigurationException e) {
			LOGGER.error("Error parsing document.", e);
			return null;
		}
	}

	/**
	 * 
	 * Sets the Message and Attachment elements when start tags of those types are encountered in the xml
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// reset
		LOGGER.trace("Start element:{}", qName);
		tempVal = "";
		tempQName = qName; // qName is the element that is currently being
							// parsed
		stringBuilder = new StringBuilder();

		if (qName.equalsIgnoreCase("Message")) {
			// create a new instance of message
			tempMessage = new Message();
			tempMessage.setDomain(attributes.getValue("domain"));
			tempMessage.setSendingSystem(attributes.getValue("sendingSystem"));
			tempMessage.setBatchSending(attributes.getValue("batchSending"));
			tempMessage.setCaseDescription(attributes.getValue("caseDescription"));
			tempMessage.setCaseOfficer(attributes.getValue("caseOfficer"));
		} else if (qName.equalsIgnoreCase("Attachment")) {
			// create a new instance of attachment
			tempAttachment = new Attachment();
			tempAttachment.setFilename(attributes.getValue("filename"));
			tempAttachment.setName(attributes.getValue("name"));
			tempAttachment.setMimeType(attributes.getValue("mimeType"));
			tempFil = new CharArrayWriter();
		}
	}

	/**
	 * 
	 * The sequence of characters currently being read by the parser
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		LOGGER.trace("Character element: {}", tempQName);
		// In the case of attachment elements the character sequence might not
		// be long enough to cover the entire
		// elements, hence the need to append the sequence to a CharArrayWriter

		if (tempQName.equalsIgnoreCase("Attachment") && (tempFil != null)) {
			tempFil.write(ch, start, length);
		} else {
			stringBuilder.append(new String(ch, start, length));
			tempVal = stringBuilder.toString();
		}
	}

	/**
	 * 
	 * Sets the various message elements when an end tag of a particular element is encountered
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		LOGGER.trace("End element:{}", qName);
		tempQName = qName; // qName is the element currently being parsed

		if (qName.equalsIgnoreCase("Idproc")) {
			tempMessage.setIdproc(tempVal);
		} else if (qName.equalsIgnoreCase("MessageReference")) {
			tempMessage.setMessageReference(tempVal);
		} else if (qName.equalsIgnoreCase("ParticipantId")) {
			tempMessage.setParticipantId(tempVal);
		} else if (qName.equalsIgnoreCase("DueDate")) {
			tempMessage.setDueDate(tempVal);
		} else if (qName.equalsIgnoreCase("AltinnArchive")) {
			tempMessage.setAltinnArchive(tempVal);
		} else if (qName.equalsIgnoreCase("MessageHeader")) {
			tempMessage.setContentMessageHeader(tempVal);
		} else if (qName.equalsIgnoreCase("MessageSummery")) {
			tempMessage.setContentMessageSummary(tempVal);
		} else if (qName.equalsIgnoreCase("Attachment")) {
			String base64EncodedString = new String(tempFil.toCharArray());
			tempAttachment.setBase64EncodedString(base64EncodedString);
			attachments.add(tempAttachment);
			LOGGER.trace("Attachment: {}", tempFil);
		} else if (qName.equalsIgnoreCase("SMSPhoneNumber")) {
			tempContactInfo = new ContactInfo();
			tempContactInfo.setType(UniversalConstants.CONTACTINFOTYPE_SMS);
			tempContactInfo.setAddress(tempVal);
			contactInfo.add(tempContactInfo);
		} else if (qName.equalsIgnoreCase("EmailAddress")) {
			tempContactInfo = new ContactInfo();
			tempContactInfo.setType(UniversalConstants.CONTACTINFOTYPE_EMAIL);
			tempContactInfo.setAddress(tempVal);
			contactInfo.add(tempContactInfo);
		} else if (qName.equalsIgnoreCase("Attachments")) {
			tempMessage.setAttachments(attachments);
		} else if (qName.equalsIgnoreCase("NotificationMessages")) {
			tempMessage.setContactInfo(contactInfo);
		}
	}
}
