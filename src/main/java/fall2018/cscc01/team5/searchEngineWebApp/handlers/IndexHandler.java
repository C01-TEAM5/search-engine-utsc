package fall2018.cscc01.team5.searchEngineWebApp.handlers;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import fall2018.cscc01.team5.searchEngineWebApp.util.ContentGenerator;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class IndexHandler {

    private StandardAnalyzer analyzer;    // Use default setting
    private Directory ramIndex;           // store index in RAM
    private IndexWriterConfig config;     // Index Writer Configurations
    private IndexWriter writer;           // Index Writer
    private String storePath;             // The path where the index will be stored
    
    /**
     * Construct a new IndexHandler. This class represents the indexer for the
     * search engine. Index is stored in RAM.
     */
    public IndexHandler (String storedPath) {
        analyzer = new StandardAnalyzer();
        ramIndex = new RAMDirectory();
        storePath = storedPath;
        config = new IndexWriterConfig(analyzer);

        // write separate IndexWriter to RAM for each IndexHandler
        // writer will have to be manually closed with each instance call
        // see IndexWriter documentation for .close()
        // explaining continuous closing of IndexWriter is an expensive operation
        try {
            writer = new IndexWriter(ramIndex, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Takes a DocFile as a parameter and adds the contents of the DocFile to the index. If an invalid document type is
     * passed in, nothing happens.
     * 
     * File types that can be passed in include: .txt, .pdf, .html, .docx
     * 
     * Precondition: DocFiles passed into addDoc must not be already indexed
     *
     * @param newFile the object of the file that will be added to the index
     */
    public void addDoc (DocFile newFile) {

        // Check if the file extension is valid
        if (!isValid(newFile)) {
            return;
        }
        
        // Create the new document, add in DocID fields and UploaderID fields
        Document newDocument = new Document();

        Field docIDField = new StringField(Constants.INDEX_KEY_PATH, newFile.getPath(), Store.YES);
        //Field docIDField = new TextField(Constants.INDEX_KEY_PATH, newFile.getPath(), Store.YES);
        Field userIDField = new TextField(Constants.INDEX_KEY_OWNER, newFile.getOwner(), Store.YES);
        Field titleField = new TextField(Constants.INDEX_KEY_TITLE, newFile.getTitle(),Store.YES);
        
        newDocument.add(docIDField);
        newDocument.add(userIDField);
        newDocument.add(titleField);

        //Call Content Generator to add in the ContentField
        ContentGenerator.generateContent(newDocument, newFile);

        // Add the Document to the Index
        try {
            writer.addDocument(newDocument);
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see if a DocFile is valid. A valid DocFile is any file with the following extensions: .pdf, .txt,
     * .docx, .html
     *
     * @param file the DocFile to check validity of
     * @return boolean true if the DocFile is a valid extension
     */
    private boolean isValid (DocFile file) {

        return Arrays.asList(Constants.VALIDDOCTYPES).contains(file.getFileType());
    }

    /**
     * Update the indexing of a file that is currently indexed. This method is called when a document is changed and
     * needs to be re-indexed.
     *
     * @param updatefile the object of the file to be updated.
     */
    public void updateDoc (DocFile updatefile) {

        // remove the file from the index and re-add it
        this.removeDoc(updatefile);
        this.addDoc(updatefile);
    }

    /**
     * Remove the specified document from the index. If the document has not been indexed, nothing happens.
     *
     * @param deletefile the object of the file to be removed from the index
     */
    public void removeDoc (DocFile deletefile) {
    	
    	Term term = new Term(Constants.INDEX_KEY_PATH, deletefile.getPath());

    	//System.out.println("delete file: " + term.field() + " " + term.text());
    	
    	try {
			writer.deleteDocuments(term);
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}   	
    }
    
    
    /**
     * Commits the changes to a local copy from the RAM without shutting down the
     * indexHandler.
     */
    public void commitWriter() {
        try {
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Commits and closes the changes to a local copy from the RAM 
     * shuts down the writer from the IndexHandler
     */
    public void closeWriter() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Searches the index using the "Title" field with a provided
     * query. Returns the results as a String to be shown to the user.
     * 
     * @param query
     * @return String results of the search.
     */
    public String searchByTitle(String query) {
        return null;
        
    }
    
    /**
     * Searches the index using the Content field with the provided
     * query. Returns the results as a String to be shown to the user.
     * 
     * @param query
     * @return String results of the search.
     */
    public String searchByContent(String query) {
        return null;
    }
    
    /**
     * Searches the index using the Owner field with the provided
     * query. Returns the results as a String to be shown to the user.
     * 
     * @param query
     * @return String results of the search.
     */
    public String searchByUser(String query) {
        return null;
    }
    
    /**
     * Helper function to the search methods. When provided an ScoreDoc array,
     * this function builds the string that will be returned to the user.
     * Each search method will use this to provide a uniform returned String
     * for all searches.
     * 
     * @param results a ScoreDoc array containing the hits
     * @return String results of the search
     */
    public String searchResponse(ScoreDoc[] results) {
        return null;
    }
    
    public StandardAnalyzer getAnalyzer() {
        return analyzer;
    }
    
    public Directory getRamIndex() {
        return ramIndex;
    }
    
    public IndexWriter getIndexWriter() {
        return writer;
    }

}
