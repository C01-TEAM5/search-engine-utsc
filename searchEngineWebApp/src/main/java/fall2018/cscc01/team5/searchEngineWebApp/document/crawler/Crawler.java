package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Crawler class based off the ImageCrawler in the Crawler4j documentation.
 * This crawler downloads all appropriate document types into a temporary folder,
 * indexes them and then removes the temporary folder.
 *
 */
public class Crawler extends WebCrawler {

    //Adjusted filters from the documentation to suit my needs
	private static final Pattern filters = Pattern.compile(
	        ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|bmp" +
	        "|rm|smil|wmv|swf|wma|zip|rar|gz|doc|gif|jpe?g|png|tiff?))$");
	
    private static final Pattern docPattern = Pattern.compile(
            ".*(\\.(pdf|txt|docx|html))$");
    
    private static File savedDocsFolder;
    
    public static void setup(String savedDocsPath) {
    	
    	savedDocsFolder = new File(savedDocsPath);
    	if (!savedDocsFolder.exists()) {
    		savedDocsFolder.mkdirs();
    	}
    	
    }
    
    public static void teardown() throws IOException {
    	
    	if (savedDocsFolder.exists()) {
    		FileUtils.deleteDirectory(savedDocsFolder);
    	}
    	
    }

    /**
     * Method to determine if a given page should be visited
     * by the crawler. Returns true if it should be visited,
     * false otherwise.
     * 
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
    	
    	String href = url.getURL().toLowerCase();
    	
    	if (filters.matcher(href).matches()) {
    		return false;
    	}
    	
    	if (docPattern.matcher(href).matches()) {
    		return true;
    	}
    	
    	try {
			boolean inSeeds = withinSeeds(referringPage, url);
			return inSeeds;
		} catch (MalformedURLException e) {
			return false;
		}
    	
    }
    
    /**
     * This method is called when a page is visited. Responsible for
     * gathering the required information and sending to the CSVWriter
     * to be written to file.
     * 
     */
    @Override
    public void visit(Page page) {
        
      
    }
    
    
    /**
     * Helper function to determine if a given url is within
     * the seed of the referring page. We want to keep all crawled
     * results within the original website where the crawl started
     * 
     * @param referringPage the page we came from (we want to stay in this seed)
     * @param url the url we have found
     * @return true if it is within the seed, false otherwise
     * @throws MalformedURLException
     */
    private boolean withinSeeds(Page referringPage, WebURL url) throws MalformedURLException {
        
        boolean withinSeed = false;
        URL newURL = new URL(url.getURL().toLowerCase());
        WebURL seedWebURL = referringPage.getWebURL();
        URL seedURL = new URL(seedWebURL.getURL().toLowerCase());
        
        if (seedURL.getHost().equals(newURL.getHost())) {
            withinSeed = true;
        }
         
        return withinSeed;
        
    }
}
