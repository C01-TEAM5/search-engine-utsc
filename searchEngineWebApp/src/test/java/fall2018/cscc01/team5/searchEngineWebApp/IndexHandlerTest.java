package fall2018.cscc01.team5.searchEngineWebApp;

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

public class IndexHandlerTest {
	
	private static StandardAnalyzer analyzer;
    private static Directory index;
    private static IndexWriterConfig config;
    private static IndexWriter w;
    
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
	
	/*
	 * mock object to search the lucene db after each add/update/remove call
	 * base code is from : http://www.lucenetutorial.com/lucene-in-5-minutes.html
	 */
    private static List<String> search(String query) {

    	List<String> results = new ArrayList<String>();
    	
		try {
			// create a query from user's query search
			Query q = new QueryParser("title", analyzer).parse(query);
			
			// get search results
	        int hitsPerPage = 10;
	        IndexReader reader = DirectoryReader.open(index);
	        IndexSearcher searcher = new IndexSearcher(reader);
	        TopDocs docs = searcher.search(q, hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;

	        // store results
	        for(int i=0;i<hits.length;++i) {
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	            results.add(d.get("title"));
	        }
	        
	        // close
	        reader.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}



}