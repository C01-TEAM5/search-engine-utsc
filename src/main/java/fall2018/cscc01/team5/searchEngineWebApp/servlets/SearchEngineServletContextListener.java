package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SearchEngineServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Closing index");
        IndexHandler.getInstance().closeWriter();
    }

    @Override
    public void contextInitialized (ServletContextEvent event) {
        // use crawler here to build the index
        System.out.println("Creating new index");
        IndexHandler.getInstance();
    }
}
