package ru.nbki.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class GetClient {
    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        final Map<String, String> query = new HashMap<>();
        query.put("FID", "102");
        query.put("Name", "Alex");
        query.put("Surname", "Ivanov");
        query.put("middlename", "Ivanovich");
        query.put("BirthDate", "11-06-1995");
        query.put("Number", "9653245564");

        try {
            HttpGet httpGet = new HttpGet("http://localhost:8080/bmi");

            HttpResponse response = httpClient.execute(httpGet);
            ResponseHandler<String> handler = new BasicResponseHandler();
//            System.out.println(response.getStatusLine());
            String body = handler.handleResponse(response);
            System.out.println(body);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
