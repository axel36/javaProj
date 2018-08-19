package ru.mbtc.guiceServlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import ru.mbtc.guiceServlet.afsRequest.ServiceAFS;
import ru.mbtc.guiceServlet.dbLog.DBlogger;
import ru.mbtc.guiceServlet.fid.FIDUtils;
import ru.mbtc.guiceServlet.data.DataHandler;
import ru.mbtc.guiceServlet.util.Client;
import ru.mbtc.guiceServlet.util.FidWithRule;
import ru.mbtc.varsvc.VariablesService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.soap.SOAPException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Singleton
public class GuiceServlet extends HttpServlet {

    private final Logger logger;
    private VariablesService variablesService;
    private final DataHandler dataHandler;
    private ServiceAFS serviceAFS;
    private FIDUtils fidUtils;
    private DBlogger dBlogger;
    //for logging
    private DataSource ds;
    boolean fl= true;

    @Inject
    public GuiceServlet(VariablesService variablesService, Logger logger, DataHandler dataHandler, ServiceAFS serviceAFS, FIDUtils fidUtils, DBlogger dBlogger, @Named("logging")DataSource dataSource) {
        super();
        this.variablesService = variablesService;
        this.logger = logger;
        this.dataHandler = dataHandler;
        this.serviceAFS = serviceAFS;
        this.fidUtils = fidUtils;
        //todo: Before using dbLogger u must create LogTable in db (see resources)
        this.dBlogger = dBlogger;

        this.ds = dataSource;
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String s = req.getParameter("say");
        Integer i = 5;
        out.print("<h1>Hello Servlet</h1>" + i.toString() + s);
        logger.info("get GET param - " + s);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        logger.info("get POST");
        dBlogger.setRequestTime();


        Map<String, String> query;
        Map<String, String> responseMap = new HashMap<>();
        PrintWriter out = resp.getWriter();
        try {

            String requestBodyJson;

            req.setCharacterEncoding("UTF-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
            requestBodyJson = reader.readLine();
            reader.close();

            try {
                query = dataHandler.getMapFromJson(requestBodyJson);

            } catch (JsonParseException e) {
                logger.info("Wrong data object \n" + e.getLocalizedMessage());
                responseMap.put("error", " wrong data object \n " + e.getLocalizedMessage());
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            }

            Client client;
            try {
                client = dataHandler.getNormalizatedClientFromMap(query);
            }catch (NullPointerException e){
                logger.info("Error with client data ");
                responseMap.put("error", "Error with client data \n " + e.getLocalizedMessage());
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            } catch (DateTimeParseException e){
                logger.info("Error with client birth Date ");
                responseMap.put("error", "Error with client birth Date \n " + e.getLocalizedMessage());
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            }

            List<FidWithRule> fidsWithRules;
            try {
                String soapEndpointUrl = "http://10.200.200.185:9093/afsnbch/ws/service";
                fidsWithRules = serviceAFS.matchClientDataWithAFSRules(client, soapEndpointUrl);
            } catch (SOAPException e) {
                logger.info("Error occurred while sending SOAP Request to Server ");
                responseMap.put("error", "Error with connecting to the Server \n ");
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            } catch (IOException e) {
                logger.info("Error occurred while creating SOAP message ");
                responseMap.put("error", "Error occurred while creating SOAP message \n ");
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            }catch (NullPointerException e){
                logger.info("Error in input data");
                responseMap.put("error", "Error in input data \n");
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            }
            if(fidsWithRules.size() == 1 && fidsWithRules.get(0).getFid() == -2){
                logger.info("Error in afs response ");
                responseMap.put("error", "Error in afs response \n " + fidsWithRules.get(0).getRule());
                out.println(dataHandler.packMapToJson(responseMap));
                out.close();
                return;
            }

            long fid;
            fid = fidUtils.findBestFID(fidsWithRules);
            logger.info("get FID - " + Long.toString(fid));
            responseMap = variablesService.apply(fid);

            String responseString = dataHandler.packMapToJson(responseMap);
            out.println(responseString);
            dBlogger.setResponseAndLogToDB(query,responseString);

        } catch (Exception e) {
            responseMap.put("error", " something went wrong \n " + e.getLocalizedMessage());
            logger.info("Something went wrong " + e.getLocalizedMessage());
            out.println(dataHandler.packMapToJson(responseMap));
        } finally {
            out.close();
        }

    }


}

