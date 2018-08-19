package ru.nbki.client;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MyHttpClient {
    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        final Map<String, String> query = new HashMap<>();
//        query.put("FID", "102");
        query.put("firstName", "ААННА");
        query.put("surName", "СИЗИКОВА");
        query.put("middleName", "АЛЕКСЕЕВНА");
        query.put("birthDate", "1988-07-29");
        query.put("number", "89603216597");

        try {
//            HttpPost httpPost = new HttpPost("http://localhost:8080/varsvc");
            HttpPost httpPost = new HttpPost("http://localhost:8080/bmi");

            String json;
            json = new ObjectMapper().writeValueAsString(query);
//            json = "{FID:\"102\",\"number\":\"9653245564\",\"surName\":\"Ivanov\",\"name\":\"Alex\",\"middleName\":\"Ivanovich\",\"birthDate\":\"YYYY-MM-DD\"}";
            StringEntity params = new StringEntity(json, "UTF-8");
            httpPost.addHeader("content-type", "application/json");
            httpPost.setEntity(params);

            HttpResponse response = httpClient.execute(httpPost);
            ResponseHandler<String> handler = new BasicResponseHandler();
//            System.out.println(response.getStatusLine());
            String body = handler.handleResponse(response);
            System.out.println(body);
            try {
                HashMap<String, String> resp =
                        new ObjectMapper().readValue(body, HashMap.class);
                for (String k : resp.keySet()) {
                    System.out.println(k + " - " + resp.get(k));
                }
            } catch (JsonParseException e) {

            }
            //System.out.println(json);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
