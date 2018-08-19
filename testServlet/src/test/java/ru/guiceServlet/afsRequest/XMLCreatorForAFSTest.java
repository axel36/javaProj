package ru.mbtc.guiceServlet.afsRequest;

import org.junit.Before;
import org.junit.Test;
import ru.mbtc.guiceServlet.util.Client;
import ru.mbtc.guiceServlet.util.HashTools;
import ru.mbtc.guiceServlet.util.NormalizeTools;

import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XMLCreatorForAFSTest {
    private String rightResponse;
    private Client testClient;
    private XMLCreatorForAFS xmlCreatorForAFS = new XMLCreatorForAFS();

    @Before
    public void setUp() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-d");
        String strdt = "1976-04-24";
        LocalDate date = LocalDate.parse(strdt, formatter);

        List<String> ph = new ArrayList<>();
        ph.add(NormalizeTools.phone("89603216598"));
        testClient = new Client(NormalizeTools.name("Петров"), NormalizeTools.name("Иван"), NormalizeTools.name("Иваныч"), date, ph);

        LocalDate now = LocalDate.now();
        formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        String strNow = now.format(formatter);
        rightResponse = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:afs=\"http://mbtc.ru/afs\" " +
                    "xmlns:app=\"http://mbtc.ru/afs/application\">" +
                    "<SOAP-ENV:Header/>" +
                    "<SOAP-ENV:Body>" +
                    "<afs:afsRequest>" +
                    "<afs:auth>" +
                    "<afs:login>varsvc</afs:login>" +
                    "<afs:password>123qweASD!</afs:password>" +
                    "</afs:auth>" +
                    "<afs:action>match</afs:action>" +
                    "<afs:ruleSetId>VARSVC_RULES</afs:ruleSetId>" +
                    "<Application>" +
                    "<id>2005499</id>" +
                    "<version>1</version>" +
                    "<date>"+ strNow + "</date>" +
                    "<memberCode>2005499</memberCode>" +
                    "<creditHistory>1</creditHistory>" +
                    "<client>" +
                    "<surname>" +
                    HashTools.hexOf(NormalizeTools.name("Петров")) +
                    "</surname>" +
                    "<firstname>" +
                    HashTools.hexOf(NormalizeTools.name("Иван")) +
                    "</firstname>" +
                    "<middlename>" +
                    HashTools.hexOf(NormalizeTools.name("Иваныч")) +
                    "</middlename>" +
                    "<birthdate>24.04.1976</birthdate>" +
                    "<phone>" +
                    "<number>" +
                    HashTools.hexOf(NormalizeTools.phone("89603216598")) +
                    "</number>" +
                    "</phone>" +
                    "</client>" +
                    "</Application>" +
                    "</afs:afsRequest>" +
                    "</SOAP-ENV:Body>" +
                    "</SOAP-ENV:Envelope>";


    }

    @Test
    public void createSOAPRequest() throws Exception {
        SOAPMessage msg = xmlCreatorForAFS.createSOAPRequest(testClient);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeTo(out);
        String actualResponse = new String(out.toByteArray());
        assertEquals(rightResponse,actualResponse);
    }

}