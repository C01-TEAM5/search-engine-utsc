package fall2018.cscc01.team5.searchEngineWebApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class IndexHandlerTest {
	
    private static IndexHandler indexHandler = null; 
    private static DocFile file = null;
    
    @Before
    public void init() {
        indexHandler = new IndexHandler("");
        file = new DocFile("hello.txt","hello","Chris","C:\\Users\\chrischow\\Documents\\hello.txt",false);
    }
    
    
	// null parameter testing
	@Test
	public void testIndexHandlerAddNull() {
		int expectedSize = search("hello").size();
		indexHandler.addDoc(null);

		// should not be able to add a null doc to the Index
		// size of search results should not change
		assertEquals(expectedSize, search("hello").size());
	}
	
	/*
	public void testIndexHandlerUpdateNull() {
		indexHandler.updateDoc(null);
		
		// should not be able to update a null doc in the Index
		// titled document should still be found with no changes
		assertTrue(search("Hello World").contains("Title"));		
	}
	
	public void testIndexHandlerRemoveNull() {
		int expectedSize = search("Hello World").size();
		indexHandler.removeDoc(null);

		// should not be able to remove a null doc from the Index
		// size of search results should not change
		assertEquals(expectedSize, search("Hello World").size());
	}
	*/
	
	// standard file types
    @Test
	public void testIndexHandlerAdd() {
		int expectedSize = search("hello").size() + 1;
		indexHandler.addDoc(file);
		int aa = search("hello").size();
		
		// should successfully add the file and increase its size by 1
		assertEquals(expectedSize, search("hello").size());
	}
    
    /*
	@Test
	public void testIndexHandlerUpdate() {
		indexHandler.updateDoc(file);
		assertTrue(search("Hello World").contains("OldTitle") &&
				search("Hello World").contains("NewTitle"));
	}

    @Test
	public void testIndexHandlerRemove() {
		int expectedSize = search("Hello World").size() - 1;
		indexHandler.removeDoc(file);

		// should successfully remove the file and decrease its size by 1
		assertEquals(expectedSize, search("Hello World").size());	
	}
	
	
	// boundary cases
	
	public void testIndexHandlerUpdateNonExisting() {
		
		indexHandler.updateDoc(file);
		
		// should not be able to update a non-existing doc in the Index
		// titled document should still be found with no changes
		assertTrue(search("Hello World").contains("Title"));
	}
	
	public void testIndexHandlerRemoveEmpty() {
		
    	// clear Lucene db ****
		
    	indexHandler.removeDoc(file);
    	
    	// should not be able to remove a document from an empty db
    	assertEquals(0, search("").size());
    }
    
    */

	
	/*
	 * mock object to search the lucene db after each add/update/remove call
	 * base code is from : http://www.lucenetutorial.com/lucene-in-5-minutes.html
	 */
    private static List<String> search(String query) {

        
    	List<String> results = new ArrayList<String>();
    	
		try {
		    indexHandler.commitWriter();
			// create a query from user's query search
			Query q = new QueryParser(Constants.INDEX_KEY_TITLE, indexHandler.getAnalyzer()).parse(query);
			
			// get search results
	        int hitsPerPage = 10;
	        IndexReader reader = DirectoryReader.open(indexHandler.getRamIndex());
	        IndexSearcher searcher = new IndexSearcher(reader);
	        TopDocs docs = searcher.search(q, hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;

	        // store results
	        for(int i=0;i<hits.length;++i) {
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	            results.add(d.get(Constants.INDEX_KEY_TITLE));
	        }
	        
	        // close
	        reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

    
    // more mock objects for testing
    // to be deleted
    
	/*
	public static void main(String[] args) throws IOException, ParseException {
        analyzer = new StandardAnalyzer();
        index = new RAMDirectory();
        config = new IndexWriterConfig(analyzer);
        w = new IndexWriter(index, config);


        //addDoc(w, "Lucene in Action");
        //addDoc(w, "Lucene for Dummies");
        //addDoc(w, "Managing Gigabytes");
        //addDoc(w, "The Art of Computer Science");
        w.close();


        System.out.println(search("asdfasfsa").size());
        System.out.println(search("lucene").size());
    }
    private static void addDoc(IndexWriter w, String title) {
        try {
	    	Document doc = new Document();
	        doc.add(new TextField("title", title, Field.Store.YES));
	        w.addDocument(doc);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    */

}