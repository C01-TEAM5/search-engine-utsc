package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

public class SearchEngineServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Closing index");
        try {
            IndexHandler.getInstance().closeWriter();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    public void contextInitialized (ServletContextEvent event) {
        // use crawler here to build the index
        System.out.println("Creating new index");
        try {
            IndexHandler.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
