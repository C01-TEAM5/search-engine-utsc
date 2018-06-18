package fall2018.cscc01.team5.searchEngineWebApp;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class IndexHandler{
	
	private static StandardAnalyzer analyzer = new StandardAnalyzer();  // Use default setting  
	private static Directory index = new RAMDirectory();    // Save Index in RAM
	private static Document doc = new Document();   // a collection of fields that can relate to the document, ie file name, uploader, type, content 
	
	private static final String uploadinfo = null;   // hard-code the file info need to index for now
	
	/* Constructor*/
	public IndexHandler() {
		
	}
	
	/* The main function to perform index functionality
	 * Can index/remove/update index all file types
	 * 
	 * Call it in SearchEngine for indexing before search
	 */
	public static void StarIndexing() throws IOException {
		//TODO
	}
	
	/* Index TXT file, including index/update/delete a file
	 * Helper function for startIndexing
	 */
	public static void indexTXT() {
		//TODO
	}
	
	/* Index HTML file, including index/update/delete a file
	 * Helper function for startIndexing
	 */
	public static void indexHTML() {
		//TODO
	}
	
	/* Index PDF file, including index/update/delete a file
	 * Helper function for startIndexing
	 */
	public static void indexPDF() {
		//TODO
	}
	
	/* Index doc file, including index/update/delete a file
	 * Helper function for startIndexing
	 */
	public static void indexDOC() {
		//TODO
	}
	
}
