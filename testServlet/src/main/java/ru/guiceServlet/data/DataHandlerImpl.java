package ru.mbtc.guiceServlet.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mbtc.guiceServlet.util.Client;
import ru.mbtc.guiceServlet.util.NormalizeTools;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataHandlerImpl implements DataHandler {
    @Override
    public Map<String, String> getMapFromJson(String json) throws IOException {
        TypeReference<HashMap<String,String>> typeRef
                = new TypeReference<HashMap<String,String>>() {};

        return new ObjectMapper().readValue(json, typeRef);
    }

    @Override
    public String packMapToJson(Map<String, String> map) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(map);
    }

    @Override
    public Client getNormalizatedClientFromMap(Map<String, String> query) throws NullPointerException{
        List<String> ph = new ArrayList<>();
        String number, surname, firstname, middlename;
        number = NormalizeTools.phone(query.get("number"));
        Objects.requireNonNull(number, "wrong phone number");
        ph.add(number);

        surname = NormalizeTools.name(query.get("surName"));
        Objects.requireNonNull(number, "wrong surname");
        firstname = NormalizeTools.name(query.get("firstName"));
        Objects.requireNonNull(number, "wrong firstname");
        middlename = NormalizeTools.name(query.get("middleName"));
        Objects.requireNonNull(number, "wrong middlename");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-d");
        String strdt = query.get("birthDate");
        LocalDate date = LocalDate.parse(strdt, formatter);

        return new Client(surname, firstname, middlename, date, ph);
    }
}
