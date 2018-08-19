package ru.mbtc.bmiservice;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.mbtc.bmiservice.data.DataHandler;
import ru.mbtc.bmiservice.service.BMIcalc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class GuiceServlet extends HttpServlet {

    private final BMIcalc bmIcalc;
    private final Logger logger;
    private final DataHandler dataHandler;

    @Inject
    public GuiceServlet(BMIcalc bmIcalc, Logger logger, DataHandler dataHandler) {
        this.bmIcalc = bmIcalc;
        this.logger = logger;
        this.dataHandler = dataHandler;
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        logger.info("new GET req");
        PrintWriter out = resp.getWriter();
        Integer i = 5;
        out.print("<h1>Hello Servlet</h1> " + i.toString());

    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        logger.info("new POST req");
        final Map<String, Integer> query;
        final Double idx;
        final String result;
        final String responseJsonString;

        String requestBodyJson;

        req.setCharacterEncoding("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        requestBodyJson = reader.readLine();
        reader.close();
        logger.info(requestBodyJson);

        try (final PrintWriter out = resp.getWriter()) {

            query = dataHandler.getMapFromJson(requestBodyJson);

            idx = bmIcalc.calcIndex(query.get("height"), query.get("weight"));
            result = bmIcalc.getTextResult(idx);
            bmIcalc.addOneRecord(dataHandler.createOneRecFromQuery(query, idx, result));

            responseJsonString = dataHandler.packListToJson(bmIcalc.getRecords());

            out.print(responseJsonString);

        } catch (JsonParseException e) {
            logger.info("Wrong data object \n" + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
