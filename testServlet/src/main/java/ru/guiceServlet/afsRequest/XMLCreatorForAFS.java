package ru.mbtc.guiceServlet.afsRequest;

import ru.mbtc.guiceServlet.util.Client;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class XMLCreatorForAFS {
    public SOAPMessage createSOAPRequest(Client client) throws SOAPException, IOException {
        String soapAction = "http://mbtc.ru/afs/match";
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        createSoapEnvelope(soapMessage, client);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();
        return soapMessage;
    }

    private void createSoapEnvelope(SOAPMessage soapMessage, Client client) throws SOAPException, NullPointerException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String rulesName = "VARSVC_RULES";

        String myNamespace = "afs";
        String myNamespaceURI = "http://mbtc.ru/afs";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
        envelope.addAttribute(new QName("xmlns:app"), "http://mbtc.ru/afs/application");
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement afsRequest = soapBody.addChildElement("afsRequest", myNamespace);
        SOAPElement auth = afsRequest.addChildElement("auth", myNamespace);

        SOAPElement login = auth.addChildElement("login", myNamespace);
        login.addTextNode("varsvc");
        SOAPElement passwd = auth.addChildElement("password", myNamespace);
        passwd.addTextNode("123qweASD!");

        SOAPElement action = afsRequest.addChildElement("action", myNamespace);
        action.addTextNode("match");
        SOAPElement rule = afsRequest.addChildElement("ruleSetId", myNamespace);
        rule.addTextNode(rulesName);
        SOAPElement application = afsRequest.addChildElement("Application");

        //todo: Date? id? memberCode? version ?
        SOAPElement id = application.addChildElement("id");
        id.addTextNode("2005499");
        SOAPElement ver = application.addChildElement("version");
        ver.addTextNode("1");
        SOAPElement date = application.addChildElement("date");
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        String strNow = now.format(formatter);
        date.addTextNode(strNow);
        SOAPElement membCode = application.addChildElement("memberCode");
        membCode.addTextNode("2005499");
        SOAPElement crHist = application.addChildElement("creditHistory");
        crHist.addTextNode("1");

        SOAPElement cl = application.addChildElement("client");
        SOAPElement surname = cl.addChildElement("surname");
        surname.addTextNode(client.getSurname());
        SOAPElement firstname = cl.addChildElement("firstname");
        firstname.addTextNode(client.getFirstname());
        SOAPElement middlename = cl.addChildElement("middlename");
        middlename.addTextNode(client.getMiddlename());
        SOAPElement birthdate = cl.addChildElement("birthdate");
        birthdate.addTextNode(client.getBirthdate());

        SOAPElement phone = cl.addChildElement("phone");

        for (String number : client.getPhones()) {
            SOAPElement num = phone.addChildElement("number");
            num.addTextNode(number);
        }

    }
}
