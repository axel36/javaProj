package ru.mbtc.guiceServlet.afsRequest;

import com.google.inject.Inject;
import ru.mbtc.guiceServlet.util.Client;
import ru.mbtc.guiceServlet.util.FidWithRule;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ServiceAFS {

    private final ResponseXMLparser responseXMLparser;
    private final Logger logger;
    private final XMLCreatorForAFS xmlCreatorForAFS;

    @Inject
    public ServiceAFS(ResponseXMLparser responseXMLparser, Logger logger, XMLCreatorForAFS xmlCreatorForAFS) {
        this.responseXMLparser = responseXMLparser;
        this.logger = logger;
        this.xmlCreatorForAFS = xmlCreatorForAFS;
    }

    public List<FidWithRule> matchClientDataWithAFSRules(Client client,String soapEndpointUrl) throws SOAPException, IOException {
        SOAPMessage requestForAFS = xmlCreatorForAFS.createSOAPRequest(client);
        SOAPMessage responseFromAFS = callSoapWebService(soapEndpointUrl,requestForAFS);
        //Logging
        try(ByteArrayOutputStream outLog = new ByteArrayOutputStream();) {

            requestForAFS.writeTo(outLog);
            logger.info("requestForAFS - "+new String(outLog.toByteArray()));
            outLog.reset();
            responseFromAFS.writeTo(outLog);
            logger.info("responseFromAFS - "+new String(outLog.toByteArray()));

        }

        return responseXMLparser.getData(responseFromAFS);
    }

    private SOAPMessage callSoapWebService(String soapEndpointUrl, SOAPMessage requestForAFS) throws SOAPException {
        final SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        final SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        try {
            final SOAPMessage soapResponse = soapConnection.call(requestForAFS, soapEndpointUrl);
            return soapResponse;
        } finally {
            soapConnection.close();
        }
    }
}
