package ru.mbtc.guiceServlet.afsRequest;

import ru.mbtc.guiceServlet.util.FidWithRule;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.List;

public interface ResponseXMLparser {
    List<FidWithRule> getData(SOAPMessage soapResponse) throws SOAPException;
}
