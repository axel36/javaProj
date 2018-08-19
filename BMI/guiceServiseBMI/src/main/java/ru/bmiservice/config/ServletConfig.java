package ru.mbtc.bmiservice.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import ru.mbtc.bmiservice.GuiceServlet;
import ru.mbtc.bmiservice.data.DataHandler;
import ru.mbtc.bmiservice.data.DataHandlerImpl;
import ru.mbtc.bmiservice.service.BMIcalc;
import ru.mbtc.bmiservice.service.BMIcalcImpl;

import java.util.HashMap;
import java.util.Map;

public class ServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(BMIcalc.class).to(BMIcalcImpl.class);
                bind(DataHandler.class).to(DataHandlerImpl.class);

                Map<String, String> guiceContainerConfig = new HashMap<>();
                serve("/bmi").with(GuiceServlet.class, guiceContainerConfig);
                filter("/*").through(CorsFilter.class);
            }
        });
    }
}
