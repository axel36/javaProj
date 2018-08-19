package ru.mbtc.guiceServlet.data;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DataHandlerImplTest {
    DataHandlerImpl juiInstance;
    Map<String, String> testMap;
    String testJsonStr;
    @Before
    public void setUp() throws Exception {

        juiInstance = new DataHandlerImpl();

        testMap = new HashMap<>();
        testMap.put("firstName", "АННА");
        testMap.put("surName", "СИЗИКОВА");
        testMap.put("middleName", "АЛЕКСЕЕВНА");


        testJsonStr = "{\"firstName\":\"АННА\",\"surName\":\"СИЗИКОВА\",\"middleName\":\"АЛЕКСЕЕВНА\"}";

    }

    @Test
    public void getMapFromJsonTest() throws Exception {
        assertEquals(testMap,juiInstance.getMapFromJson(testJsonStr));
    }

    @Test
    public void packMapToJson() throws Exception {
        assertEquals(testJsonStr,juiInstance.packMapToJson(testMap));
    }

}