package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

public class CrawlerFactory implements CrawlController.WebCrawlerFactory {

    private String currentUser;
    private String courseCode;
    
    public CrawlerFactory(String currentUser, String courseCode) {
        
        this.currentUser = currentUser;
        this.courseCode = courseCode;
        
    }
    
    @Override
    public WebCrawler newInstance() throws Exception {

        return new Crawler(currentUser, courseCode);
    }

}
