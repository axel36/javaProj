package ru.nbki.client;


import org.jetbrains.annotations.Contract;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.nbki.Client;
import ru.nbki.FidWithRule;

import javax.swing.text.html.HTMLDocument;
import javax.xml.namespace.QName;
import javax.xml.soap.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SoapClient {
    // SAAJ - SOAP Client Testing
    public static void main(String args[]) throws javax.xml.soap.SOAPException {

        String soapEndpointUrl = "http://10.200.200.185:9093/afsnbch/ws/service";
        String soapAction = "http://mbtc.ru/afs/match";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-d");
        String strdt = "1976-04-24";
        LocalDate date = LocalDate.parse(strdt, formatter);

        List<String> ph = new ArrayList<>();
        ph.add("89603216598");
        Client client = new Client("АБРАМОВА777", "ЕЛЕНА77", "СЕРГЕЕВНА", date, ph);

        List<FidWithRule> fidsWithRules = callSoapWebService(soapEndpointUrl, soapAction, client);
        System.out.println(findFID(fidsWithRules));
    }


    private static long findFID(List<FidWithRule> fidsWithRules) {
        if (fidsWithRules.size() < 1) {
            return -1;
        }
        if (fidsWithRules.size() == 1) {
            return fidsWithRules.get(0).getFid();
        }
        List<FidWithRule> listWithOneRule = new ArrayList<>(fidsWithRules.size());
        List<FidWithRule> listWithAnotherRule = new ArrayList<>(fidsWithRules.size());
        List<FidWithRule> listWithThirdRule = new ArrayList<>(fidsWithRules.size());
        for (FidWithRule f : fidsWithRules) {
            if (f.getRule().equals("RULE_1"))
                listWithOneRule.add(f);
            if (f.getRule().equals("RULE_1a"))
                listWithAnotherRule.add(f);
        }
        if (listWithOneRule.size() > 0) {
            if (listWithOneRule.size() == 1)
                return listWithOneRule.get(0).getFid();
            else {
                return listWithAnotherRule
                        .stream()
                        .max(Comparator.comparing(FidWithRule::getFid))
                        .orElseThrow(NoSuchElementException::new)
                        .getFid();
            }
        }

        listWithAnotherRule.clear();
        listWithOneRule.clear();
        for (FidWithRule f : fidsWithRules) {
            if (f.getRule().equals("RULE_2"))
                listWithOneRule.add(f);
            if (f.getRule().equals("RULE_2a"))
                listWithAnotherRule.add(f);
            if (f.getRule().equals("RULE_2a_i"))
                listWithThirdRule.add(f);
        }
        if (listWithOneRule.size() > 0) {
            if (listWithOneRule.size() == 1) {
                return listWithOneRule.get(0).getFid();
            } else if (listWithAnotherRule.size() == 1) {
                return listWithAnotherRule.get(0).getFid();
            } else {
                return listWithThirdRule
                        .stream()
                        .max(Comparator.comparing(FidWithRule::getFid))
                        .orElseThrow(NoSuchElementException::new)
                        .getFid();
            }
        }

        listWithOneRule.clear();
        for (FidWithRule f : fidsWithRules)
            if (f.getRule().equals("RULE_3"))
                listWithOneRule.add(f);

        if (listWithOneRule.size() > 0)
            return listWithOneRule
                    .stream()
                    .max(Comparator.comparing(FidWithRule::getFid))
                    .orElseThrow(NoSuchElementException::new)
                    .getFid();
        else
            return -1;
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, Client client) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

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
        rule.addTextNode("TEST_RULES");
        SOAPElement application = afsRequest.addChildElement("Application");

        SOAPElement id = application.addChildElement("id");
        id.addTextNode("B00001");
        SOAPElement date = application.addChildElement("date");
        date.addTextNode("266.06.2018");
        SOAPElement membCode = application.addChildElement("memberCode");
        membCode.addTextNode("A00001000000");

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


    private static List<FidWithRule> callSoapWebService(String soapEndpointUrl, String soapAction, Client client) throws javax.xml.soap.SOAPException {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, client), soapEndpointUrl);
            soapConnection.close();
            NodeList respNodes = soapResponse.getSOAPBody().getChildNodes().item(1).getChildNodes();
            NodeList matchResults = respNodes;

            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();


            int i = 0;
            for (; i < respNodes.getLength(); i++) {
                if (respNodes.item(i).getNodeType() == Node.ELEMENT_NODE && respNodes.item(i).getLocalName().equals("matchResult")) {
                    matchResults = respNodes.item(i).getChildNodes();
                    break;
                }
            }
            i = 0;
            List<FidWithRule> fidWithRules = new ArrayList<>();
            for (; i < matchResults.getLength(); i++) {
                if (matchResults.item(i).getNodeType() == Node.ELEMENT_NODE) {
//                    System.out.println(matchResults.item(i).getChildNodes().item(1).getTextContent());
                    NodeList oneMatch = matchResults.item(i).getChildNodes();
                    long fid = Long.parseLong(oneMatch.item(1).getTextContent());
                    String rule = oneMatch.item(5).getTextContent();
                    fidWithRules.add(new FidWithRule(fid, rule));
//                    System.out.println(new FidWithRule(fid, rule));
                }
            }

//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            soapResponse.writeTo(stream);
//            String message = new String(stream.toByteArray(), "utf-8");
//            System.out.println(message);

            // Print the SOAP Response



            return fidWithRules;
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static SOAPMessage createSOAPRequest(String soapAction, Client client) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, client);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        String strMsg = new String(out.toByteArray());

        System.out.println("\n\n"+strMsg+"\n\n");

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }


}

