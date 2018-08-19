package ru.mbtc.guiceServlet.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import ru.mbtc.guiceServlet.GuiceServlet;
import ru.mbtc.guiceServlet.afsRequest.ResponseXMLParserImpl;
import ru.mbtc.guiceServlet.afsRequest.ResponseXMLparser;
import ru.mbtc.guiceServlet.dbLog.DBlogger;
import ru.mbtc.guiceServlet.dbLog.DBloggerImpl;
import ru.mbtc.guiceServlet.fid.FIDUtils;
import ru.mbtc.guiceServlet.fid.FIDUtilsImpl;
import ru.mbtc.guiceServlet.data.DataHandler;
import ru.mbtc.guiceServlet.data.DataHandlerImpl;
import ru.mbtc.varsvc.VariablesService;
import ru.mbtc.varsvc.impl.VariablesServiceImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import static com.google.inject.jndi.JndiIntegration.fromJndi;

public class ServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {

        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/varsvc").with(GuiceServlet.class);
                bind(VariablesService.class).to(VariablesServiceImpl.class);
                bind(Context.class).to(InitialContext.class);
                bind(DataSource.class).annotatedWith(Names.named("mainDB")).toProvider(fromJndi(DataSource.class, "java:/comp/env/jdbc/WorklightDS"));
                bind(DataSource.class).annotatedWith(Names.named("logging")).toProvider(fromJndi(DataSource.class, "java:/comp/env/jdbc/LogDBase"));
                bind(DataHandler.class).to(DataHandlerImpl.class);
                bind(ResponseXMLparser.class).to(ResponseXMLParserImpl.class);
                bind(FIDUtils.class).to(FIDUtilsImpl.class);
                bind(DBlogger.class).to(DBloggerImpl.class);

            }
        });
    }
}