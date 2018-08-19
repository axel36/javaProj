package ru.mbtc.guiceServlet.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mbtc.guiceServlet.util.Client;

import java.io.IOException;
import java.util.Map;

public interface DataHandler {
    Map<String,String> getMapFromJson(String json) throws IOException;
    String packMapToJson(Map<String, String> map) throws JsonProcessingException;
    public Client getNormalizatedClientFromMap(Map<String, String> query) throws NullPointerException;
}
