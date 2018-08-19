package ru.mbtc.guiceServlet.afsRequest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.mbtc.guiceServlet.util.FidWithRule;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;

public class ResponseXMLParserImpl implements ResponseXMLparser {
    @Override
    public List<FidWithRule> getData(SOAPMessage soapResponse) throws SOAPException {
        NodeList bodyNodes = soapResponse.getSOAPBody().getChildNodes();
        NodeList respNodes = null;
        long fid = -1;
        String rule = "none";
        List<FidWithRule> fidWithRules = new ArrayList<>();

        if(soapResponse.getSOAPPart().getEnvelope().getBody().getFault() != null){
            fidWithRules.add(
                    new FidWithRule(-2, soapResponse.getSOAPPart().getEnvelope().getBody().getFault().getFaultString()));
            return fidWithRules;
        }
        for (int i = 0; i< bodyNodes.getLength();i++) {
            if (bodyNodes.item(i).getNodeType() == Node.ELEMENT_NODE && bodyNodes.item(i).getLocalName().equals("afsResponse")){
                respNodes = bodyNodes.item(i).getChildNodes();
            }
        }

        NodeList matchResults = null;

        if (respNodes == null) throw new AssertionError();
        for (int i = 0; i < respNodes.getLength(); i++) {
            if (respNodes.item(i).getNodeType() == Node.ELEMENT_NODE && respNodes.item(i).getLocalName().equals("matchResult")) {
                matchResults = respNodes.item(i).getChildNodes();
                break;
            }
        }

        if (matchResults == null) throw new AssertionError();
        for (int i = 0; i < matchResults.getLength(); i++) {
            if (matchResults.item(i).getNodeType() == Node.ELEMENT_NODE) {


                NodeList oneMatch = matchResults.item(i).getChildNodes();
                for(int j=0;j<oneMatch.getLength();j++) {
                    if (oneMatch.item(j).getNodeType() == Node.ELEMENT_NODE && oneMatch.item(j).getLocalName().equals("appId"))
                        fid = Long.parseLong(oneMatch.item(j).getTextContent());

                    if (oneMatch.item(j).getNodeType() == Node.ELEMENT_NODE && oneMatch.item(j).getLocalName().equals("rule"))
                            rule = oneMatch.item(j).getTextContent();
                }
                fidWithRules.add(new FidWithRule(fid, rule));

            }
        }

        return fidWithRules;

    }

}
