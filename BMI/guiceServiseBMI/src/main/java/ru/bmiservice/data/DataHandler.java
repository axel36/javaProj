package ru.mbtc.bmiservice.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mbtc.bmiservice.entities.OneRecord;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataHandler {
    Map<String,Integer> getMapFromJson(String json) throws IOException;
    String packListToJson(List<OneRecord> list) throws JsonProcessingException;
    OneRecord createOneRecFromQuery(Map<String, Integer> query, Double index, String result) throws NullPointerException;
}
