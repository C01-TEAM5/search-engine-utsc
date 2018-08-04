package fall2018.cscc01.team5.searchEngineWebApp.document;

import org.junit.Before;
import org.junit.Test;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import fall2018.cscc01.team5.searchEngineWebApp.document.crawler.Crawler;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/* Test cases modified from A2 assignment tests.
 * 
 */
public class CrawlerTest {

    String currentUser = "crawluser0";
    String courseCode = "";
    private Crawler crawler;
    
    @Before
    public void setUp() {
        
        crawler = new Crawler(currentUser, courseCode);
        
    }
    
    
    /* Test cases for pages that should not be be visited
     * because of inappropriate file types.
     * 
     */
    @Test
    public void shouldNotVistType() {
        
        WebURL pageUrl = new WebURL();
        pageUrl.setURL("https://www.uoft.com");
        Page page = mock(Page.class); //will be kept in same seed for these tests so only type is tested
        WebURL url = mock(WebURL.class); //the url being checked
        
        when(page.getWebURL()).thenReturn(pageUrl);
        when(url.getURL()).thenReturn("https://www.uoft.com/song.mp3");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
        when(url.getURL()).thenReturn("https://www.uoft.com/hello.css");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
        when(url.getURL()).thenReturn("https://www.uoft.com/testcase/dancing.mov");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
        when(url.getURL()).thenReturn("https://www.uoft.com/skydiving/swimming/movie.avi");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
    }
    
    /* Test cases for pages that should not be visited because
     * they are out of the required domain.
     * 
     */
    @Test
    public void shouldNotVisitDomain() {
        
        WebURL pageUrl = new WebURL();
        pageUrl.setURL("https://www.utoronto.ca");
        Page page = mock(Page.class); //we will make the starting domain https://www.utoronto.ca
        WebURL url = mock(WebURL.class); //the url being checked
        
        when(page.getWebURL()).thenReturn(pageUrl);
        when(url.getURL()).thenReturn("https://www.reddit.com/utoronto");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
        when(url.getURL()).thenReturn("https://www.utoronto.com/toronto");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
        when(url.getURL()).thenReturn("https://www.google.com/homepage");       
        assertEquals(crawler.shouldVisit(page,url),false);
        
    }
    
    /* Test cases for pages that should be visited because
     * they are in the required domain AND of the required extension.
     * 
     */
    @Test
    public void shouldVisit() {
        
        WebURL pageUrl = new WebURL();
        pageUrl.setURL("https://www.utoronto.ca");
        Page page = mock(Page.class);
        WebURL url = mock(WebURL.class); //the url being checked
        when(page.getWebURL()).thenReturn(pageUrl);
        
        //Check with different http style
        when(url.getURL()).thenReturn("http://www.utoronto.ca/");
        assertEquals(crawler.shouldVisit(page,url),true);
        
        //We do want to index this even though it is outside the domain since it comes from
        //a page within our domain
        when(url.getURL()).thenReturn("http://www.github.com/index.html");
        assertEquals(crawler.shouldVisit(page,url),true);
        
        when(url.getURL()).thenReturn("https://www.utoronto.ca/csc343/txt.pdf");
        assertEquals(crawler.shouldVisit(page,url),true);
        
    }

    
}
