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
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
     * <p>
     * File types that can be passed in include: .txt, .pdf, .html, .docx
     * <p>
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
        String filePath = newFile.getPath();
        Field docIDField = new TextField(Constants.INDEX_KEY_PATH, filePath, Store.YES);
        Field userIDField = new TextField(Constants.INDEX_KEY_OWNER, newFile.getOwner(), Store.YES);

        newDocument.add(docIDField);
        newDocument.add(userIDField);

        //Call Content Generator to add in the ContentField
        ContentGenerator.generateContent(newDocument, newFile);

        // Add the Document to the Index
        try {
            writer.addDocument(newDocument);
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
