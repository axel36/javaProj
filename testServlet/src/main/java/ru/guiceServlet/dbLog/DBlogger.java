package ru.mbtc.guiceServlet.dbLog;

import ru.mbtc.guiceServlet.util.Client;

import java.util.Map;

public interface DBlogger {
    void setRequestTime();
    void setResponseAndLogToDB(Map<String, String> map, String resp);

}
