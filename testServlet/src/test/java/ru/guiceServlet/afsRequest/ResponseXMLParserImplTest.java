package ru.mbtc.guiceServlet.afsRequest;

import org.junit.Before;
import org.junit.Test;
import ru.mbtc.guiceServlet.util.Client;
import ru.mbtc.guiceServlet.util.FidWithRule;

import javax.xml.namespace.QName;
import javax.xml.soap.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ResponseXMLParserImplTest {

    private SOAPMessage soapMessage;
    private ResponseXMLParserImpl parser= new ResponseXMLParserImpl();
    private List<FidWithRule> rightList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        soapMessage = messageFactory.createMessage();
    }

    private void createSoapEnvelopeTrue(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "afs";
        String myNamespaceURI = "http://mbtc.ru/afs";
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement afsResponse = soapBody.addChildElement("afsResponse", myNamespace);

        SOAPElement action = afsResponse.addChildElement("action", myNamespace);
        action.addTextNode("match");


        SOAPElement matchResult = afsResponse.addChildElement("matchResult", myNamespace);

        for(int i = 100; i<103;i++) {
            SOAPElement match = matchResult.addChildElement("match", myNamespace);
            SOAPElement appid = match.addChildElement("appId", myNamespace);
            appid.addTextNode(Integer.toString(i));

            SOAPElement appVer = match.addChildElement("appVersion", myNamespace);
            appVer.addTextNode(Integer.toString(1));

            SOAPElement rule = match.addChildElement("rule", myNamespace);
            rule.addTextNode("TEST.RULE_1");

            rightList.add(new FidWithRule(i,"TEST.RULE_1"));
        }
        for(int i = 200; i<203;i++) {
            SOAPElement match = matchResult.addChildElement("match", myNamespace);
            SOAPElement appid = match.addChildElement("appId", myNamespace);
            appid.addTextNode(Integer.toString(i));

            SOAPElement appVer = match.addChildElement("appVersion", myNamespace);
            appVer.addTextNode(Integer.toString(1));

            SOAPElement rule = match.addChildElement("rule", myNamespace);
            rule.addTextNode("TEST.RULE_2");

            rightList.add(new FidWithRule(i,"TEST.RULE_2"));
        }

        for(int i = 300; i<303;i++) {
            SOAPElement match = matchResult.addChildElement("match", myNamespace);
            SOAPElement appid = match.addChildElement("appId", myNamespace);
            appid.addTextNode(Integer.toString(i));

            SOAPElement appVer = match.addChildElement("appVersion", myNamespace);
            appVer.addTextNode(Integer.toString(1));

            SOAPElement rule = match.addChildElement("rule", myNamespace);
            rule.addTextNode("TEST.RULE_3");
            rightList.add(new FidWithRule(i,"TEST.RULE_3"));
        }
    }
    private void createSoapEnvelopeFail(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "afs";
        String myNamespaceURI = "http://mbtc.ru/afs";
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        soapBody.addFault(QName.valueOf("soapenv:Client"), "RuntimeException: empty application date for id = 200549:2005499");
        rightList.add(new FidWithRule(-2, "RuntimeException: empty application date for id = 200549:2005499"));

    }


        @Test
    public void getDataTrueTest() throws Exception {
        createSoapEnvelopeTrue(soapMessage);
        soapMessage.saveChanges();
        List<FidWithRule> testList = parser.getData(soapMessage);
        if (testList.size() != rightList.size()){
            fail();
        }
        for(int i = 0; i< rightList.size();i++){
            assertEquals(rightList.get(i),testList.get(i));
        }
    }

    @Test
    public void getDataFailTest() throws Exception {
        createSoapEnvelopeFail(soapMessage);
        soapMessage.saveChanges();
        List<FidWithRule> testList = parser.getData(soapMessage);
        if (testList.size() != rightList.size()){
            fail();
        }
        for(int i = 0; i< rightList.size();i++){
            assertEquals(rightList.get(i),testList.get(i));
        }
    }

}