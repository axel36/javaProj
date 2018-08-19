package ru.mbtc.bmiservice.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mbtc.bmiservice.entities.OneRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandlerImpl implements DataHandler {
    @Override
    public Map<String, Integer> getMapFromJson(String json) throws IOException {
        final TypeReference<HashMap<String, Integer>> typeRef
                = new TypeReference<HashMap<String, Integer>>() {
        };

        return new ObjectMapper().readValue(json, typeRef);
    }

    @Override
    public String packListToJson(List<OneRecord> list) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(list);
    }

    @Override
    public OneRecord createOneRecFromQuery(Map<String, Integer> query, Double index, String result) throws NullPointerException {

        final Integer age = query.get("age");
        final Gender gender = query.get("gender") == 1 ? Gender.Male : Gender.Female;
        final Integer height = query.get("height");
        final Integer weight = query.get("weight");
        return new OneRecord(age, gender, height, weight, index, result);
    }

}
