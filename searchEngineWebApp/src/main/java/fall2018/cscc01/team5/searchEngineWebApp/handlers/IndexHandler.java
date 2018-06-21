package fall2018.cscc01.team5.searchEngineWebApp.handlers;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.util.ContentGenerator;
import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class IndexHandler{
	
    private static StandardAnalyzer analyzer;  // Use default setting  
    private static Directory ramIndex;    //store index in RAM
    private static String storePath;	//The path where the index will be stored
    private static final String[] VALIDDOCTYPES = {"pdf", "txt", "html", "docx"};
	
    /**
     * Construct a new IndexHandler.
     * This class represents the indexer for the search engine.
     * Index is stored in RAM.
     */
    public IndexHandler(String storedPath) {
        analyzer = new StandardAnalyzer();
        ramIndex = new RAMDirectory();
        storePath = storedPath;
    }
	
    /** 
     * Takes a DocFile as a parameter and adds the contents of the DocFile
     * to the index. If an invalid document type is passed in, nothing happens.
     * 
     * File types that can be passed in include:
     * .txt, .pdf, .html, .docx
     * 
     * Precondition: DocFiles passed into addDoc must not be already indexed
     * 
     * @param DocFile the object of the file that will be added to the index
     */
    public void addDoc(DocFile newFile) {
		
        //Check if the file extension is valid
        if (!isValid(newFile)) {
            return;
        }
		
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        //Create the new document, add in DocID fields and UploaderID fields
        Document newDocument = new Document();
        String filePath = newFile.getPath();
        Field docIDField = new TextField("Path", filePath, Store.YES);
        Field userIDField = new TextField("Owner",newFile.getOwner(),Store.YES);
		
        newDocument.add(docIDField);
        newDocument.add(userIDField);
		
        //Call Content Generator to add in the ContentField
        ContentGenerator.generateContent(newDocument, newFile);
		
        //Add the Document to the Index
        try {
            IndexWriter writer = new IndexWriter(ramIndex, config);
            writer.addDocument(newDocument);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     *  Checks to see if a DocFile is valid. A valid DocFile is any file with
     * the following extensions: .pdf, .txt, .docx, .html
     * 
     * @param file the DocFile to check validity of
     * @return boolean true if the DocFile is a valid extension
     */
    private static boolean isValid(DocFile file) {
		
        return Arrays.asList(VALIDDOCTYPES).contains(file.getFileType()); 
		
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
    
    public Directory getRamIndex() {
        return ramIndex;
    }

}
