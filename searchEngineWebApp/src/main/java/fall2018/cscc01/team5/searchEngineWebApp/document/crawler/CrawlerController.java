package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Crawler Controller used to establish the correct
 * settings for a document crawl. Also used to start the
 * crawl once everything has been setup.
 */
public class CrawlerController {
	
	private CrawlController controller;
	private String seed;
	private int depth;
	private int pages;

	/**
	 * Create a new CrawlerController with the target seed, 
	 * depth and pages to check.
	 * 
	 * @param seed the seed that the crawler will crawl
	 * @param depth the depth that the crawler will crawl
	 * @param pages the max number of pages the crawler will crawl
	 * @throws Exception 
	 */
	public CrawlerController(String seed, int depth, int pages) throws Exception {
		
		this.seed = seed;
		this.depth = depth;
		this.pages = pages;
		
		CrawlConfig config = new CrawlConfig();
		config.setMaxDepthOfCrawling(depth);
		config.setMaxPagesToFetch(pages);
		config.setIncludeBinaryContentInCrawling(true);
		
		//Create the controller for the crawl
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        
        this.controller = new CrawlController(config, pageFetcher, robotstxtServer);
        this.controller.addSeed(seed);
		
	}
	
	/**
	 * Starts the crawl with the given controller setup.
	 * 
	 */
	public void startCrawl() {
		
		controller.start(Crawler.class, 1);
		
	}	
	
}
