package fall2018.cscc01.team5.searchEngineWebApp;

import org.apache.lucene.document.Document;

/**
 * Class used to parse the text from different
 * types of files. Each parser return a Document
 * that can then be used by the IndexHandler for
 * indexing purposes.
 *
 */
public class Parser {

	/**
	 * Parses an HTML file and returns it in Document form.
	 * Returns an empty document if the file can't be parsed.
	 * 
	 * @param parsefile the HTML DocFile object to be parsed
	 * @return Document the HTML file in Document form
	 */
	public static Document parseHTML(DocFile parsefile) {
		return null;
		
	}
	
	/**
	 * Parses a .txt file and returns it in Document form.
	 * Returns an empty document if the file can't be parsed.
	 * 
	 * @param parsefile the .txt DocFile object to be parsed
	 * @return Document the .txt file in Document form
	 */
	public static Document parseTXT(DocFile parsefile) {
		return null;
		
	}

	/**
	 * Parses a PDF file and returns it in Document form.
	 * Returns an empty document if the file can't be parsed.
	 * 
	 * @param parsefile the PDF DocFile object to be parsed
	 * @return Document the PDF file in Document form
	 */
	public static Document parsePDF(DocFile parsefile) {
		return null;
		
	}
	
	/**
	 * Parses a DOCX file and returns it in Document form.
	 * Returns an empty document if the file can't be parsed.
	 * 
	 * @param parsefile the DOCX DocFile object to be parsed
	 * @return Document the DOCX file in Document form
	 */
	public static Document parseDOCX(DocFile parsefile) {
		return null;
		
	}
	
}
