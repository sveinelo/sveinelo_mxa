package no.mxa.ws.webservice;

import javax.inject.Inject;
import javax.jws.WebService;

@WebService(targetNamespace = "http://webservice.ws.altut.patent.siriusit.com/", serviceName = "MXA", portName = "MXAPort", wsdlLocation = "no/mxa/ws/xml/wsdl/MXA.wsdl")
public class MXADelegate {
	private MXA mxa;

	@Inject
	public MXADelegate(MXA mxa) {
		this.mxa = mxa;
	}

	public int submitMessage(String message) {
		return mxa.submitMessage(message);
	}

}