package fall2018.cscc01.team5.searchEngineWebApp.server;

import fall2018.cscc01.team5.searchEngineWebApp.document.FileManager;
import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

public class SearchEngineServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("[DEBUG] Closing index");
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
        System.out.println("[DEBUG] Creating new index");
        try {
            final IndexHandler ih = IndexHandler.getInstance();
            FileManager.indexFiles(ih);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
