package fall2018.cscc01.team5.searchEngineWebApp.handlers;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class IndexHandler{
	
	private static StandardAnalyzer analyzer = new StandardAnalyzer();  // Use default setting  
	private static Directory index = new RAMDirectory();    // Save Index in RAM
	
	private static final String uploadinfo = null;   // hard-code the file info need to index for now
	
	/**
	 * Construct a new IndexHandler.
	 * This class represents the indexer for the search engine.
	 * Index is stored in RAM.
	 */
	public IndexHandler() {
		
	}
	
	/** 
	 * Takes a DocFile as a parameter and adds the contents of the DocFile
	 * to the index.
	 * 
	 * File types that can be passed in include:
	 * .txt, .pdf, .html, .docx
	 * 
	 * @param DocFile the object of the file that will be added to the index
	 */
	public void addDoc(DocFile newfile) {
		
		//Depending on the file type, call the appropriate parse command
		//to get the required Document
		
		//Add the Document to the Index

	}
	
	/**
	 * Update the indexing of a file that is currently indexed.
	 * This method is called when a document is changed and needs to
	 * be re-indexed.
	 * 
	 * @param DocFile the object of the file to be updated.
	 */
	public static void updateDoc(DocFile updatefile) {
		//TODO
	}
	
	/** 
	 * Remove the specified document from the index.
	 * If the document has not been indexed, nothing happens.
	 * 
	 * @param DocFile the object of the file to be removed from the index
	 */
	public void removeDoc(DocFile deletefile) {
		
	}

}
