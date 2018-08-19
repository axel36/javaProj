package ru.mbtc.bmiservice.data;

import org.junit.Before;
import org.junit.Test;
import ru.mbtc.bmiservice.entities.OneRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class DataHandlerImplTest {
    private DataHandlerImpl tesHandler;
    private Map<String, Integer> testMap;
    private String testJsonStr;

    @Before
    public void setUp() throws Exception {

        tesHandler = new DataHandlerImpl();
        testMap = new HashMap<>();
        testMap.put("weight", 55);
        testMap.put("age", 18);
        testMap.put("height", 155);

    }

    @Test
    public void getMapFromJsonTest() throws Exception {
        testJsonStr = "{\"weight\":55,\"age\":18,\"height\":155}";
        assertEquals(testMap,tesHandler.getMapFromJson(testJsonStr));
    }



    @Test
    public void packListToJson() throws Exception {
        List<OneRecord> records = new ArrayList<>();
        Date now = new Date();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        String time = df.format(now.getTime());
        testJsonStr = "[{\"age\":18,\"gender\":\"Female\",\"height\":155,\"weight\":55,\"index\":10.0,\"result\":\"ok\",\"date\":\"" + time + "\"},{\"age\":19,\"gender\":\"Male\",\"height\":999,\"weight\":999,\"index\":99.0,\"result\":\"MMM\",\"date\":\"" + time + "\"}]";
        records.add(new OneRecord(18, Gender.Female, 155, 55, 10.0, "ok", now));
        records.add(new OneRecord(19, Gender.Male, 999, 999, 99.0, "MMM", now));

        String test = tesHandler.packListToJson(records);
        assertEquals(testJsonStr, test);

    }

}