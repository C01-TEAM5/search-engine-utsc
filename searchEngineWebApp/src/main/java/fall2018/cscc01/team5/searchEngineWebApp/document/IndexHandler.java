package fall2018.cscc01.team5.searchEngineWebApp.document;

import com.mongodb.*;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.lucene.store.RAMDirectory;
import org.lumongo.storage.lucene.DistributedDirectory;
import org.lumongo.storage.lucene.MongoDirectory;

public class IndexHandler {
    
    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);

    private static IndexHandler indexHandler;

    private StandardAnalyzer analyzer;    // Use default setting
    private Directory indexDir;           // store index in Local dir
    private IndexWriterConfig config;     // Index Writer Configurations
    private IndexWriter writer;           // Index Writer
    //private String storePath;             // The path where the index will be stored
    private int hitsPerPage = 1000000;

    /**
     * Construct a new IndexHandler. This class represents the indexer for the search engine. Index is stored in RAM.
     */
    private IndexHandler (boolean test) throws IOException {
        analyzer = new StandardAnalyzer();
        indexDir = test ? new RAMDirectory() : new DistributedDirectory(new MongoDirectory(mongoClient, "search-engine", "index"));
        //storePath = storedPath;
        config = new IndexWriterConfig(analyzer);

        // write separate IndexWriter to RAM for each IndexHandler
        // writer will have to be manually closed with each instance call
        // see IndexWriter documentation for .close()
        // explaining continuous closing of IndexWriter is an expensive operation
        try {
            writer = new IndexWriter(indexDir, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the shared instance of this index handler.
     *
     * @return a shared IndexHandler
     */
    public static IndexHandler getInstance () throws IOException {
        if (indexHandler == null) {
            indexHandler = new IndexHandler(false);
        }
        return indexHandler;
    }

    public static  IndexHandler getTestInstance() throws  IOException {
        return  new IndexHandler(true);
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
        if (!isValid(newFile) ) {
            return;
        }

        // Create the new document, add in DocID fields and UploaderID fields
        org.apache.lucene.document.Document newDocument = new Document();

        Field docIDField = new StringField(Constants.INDEX_KEY_ID, newFile.getId(), Store.YES);
        Field docPathField = new StringField(Constants.INDEX_KEY_PATH, newFile.getPath(), Store.YES);
        Field userIDField = new StringField(Constants.INDEX_KEY_OWNER, newFile.getOwner(), Store.YES);
        Field filenameField = new TextField(Constants.INDEX_KEY_FILENAME, newFile.getFilename(), Store.YES);
        Field isPublicField = new TextField(Constants.INDEX_KEY_STATUS, newFile.isPublic().toString(), Store.YES);
        Field titleField = new TextField(Constants.INDEX_KEY_TITLE, newFile.getTitle(), Store.YES);
        Field typeField = new TextField(Constants.INDEX_KEY_TYPE, newFile.getFileType(), Store.YES);
        Field permissionField = new TextField(Constants.INDEX_KEY_PERMISSION, Integer.toString(newFile.getPermission()), Store.YES);
        Field courseField = new TextField(Constants.INDEX_KEY_COURSE, newFile.getCourseCode(), Store.YES);
       
        
        newDocument.add(docIDField);
        newDocument.add(docPathField);
        newDocument.add(userIDField);
        newDocument.add(filenameField);
        newDocument.add(isPublicField);
        newDocument.add(titleField);
        newDocument.add(typeField);
        newDocument.add(permissionField);
        newDocument.add(courseField);
        
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
     * Update the indexing of a file that is currently indexed. This method is called when a document is changed and
     * needs to be re-indexed.
     *
     * @param updatefile the object of the file to be updated.
     */
    public void updateDoc (DocFile updatefile) throws ParseException, IOException {

        // Check if the file extension is valid
        if (updatefile == null) return;
        if (!isValid(updatefile) || !(FileManager.fileExists(updatefile.getId(), updatefile.getFileType()) || pathExists (updatefile.getPath()))) {
            return;
        }

        if (FileManager.fileExists(updatefile.getId(), updatefile.getFileType())) {
            String path = FileManager.download(updatefile.getId(), updatefile.getFileType());
            updatefile.setPath(path);
        }

        // remove the file from the index and re-add it
        this.removeDoc(updatefile);
        this.addDoc(updatefile);
        //FileManager.cleanTemporaryDownloads();
    }

    /**
     * Remove the specified document from the index. If the document has not been indexed, nothing happens.
     *
     * @param deletefile the object of the file to be removed from the index
     */
    public void removeDoc (DocFile deletefile) throws ParseException {

        // Check if the file extension is valid
        if (deletefile == null) return;
        if (!isValid(deletefile)) {
            return;
        }

        Query query = new QueryParser(Constants.INDEX_KEY_ID, analyzer).parse(deletefile.getId());
        //System.out.println("delete file: " + term.field() + " " + term.text());

        // Check if path exists
        if (FileManager.fileExists(deletefile.getId(), deletefile.getFileType()) || pathExists (deletefile.getPath())) {
            // remove doc if exits
            try {
                writer.deleteDocuments(query);
                writer.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }


    /**
     * Check if the path exists in Index Dir.
     *
     * @param path the path to check if it exists in Index Directory.
     * @return true if it exists. Otherwise return false.
     */
    public boolean pathExists (String path) {

        File file = new File(path);

        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * Check if a file exists in the index using the file's id
     *
     * @param id
     * @return
     * @throws ParseException
     */
    public boolean fileExists (String id) throws ParseException {
        
        Query query = new QueryParser(Constants.INDEX_KEY_ID, analyzer).parse(id);
        
        return searchResponse(searchExec(query), query).length > 0;
    }

    /**
     * Search for DocFile by id
     *
     * @param id the id to search
     * @return a list if docfiles containing this id
     * @throws ParseException
     */
    public DocFile[] searchById(String id, String[] fileTypes) throws ParseException, IOException {
        if (indexDir.listAll().length < 2) return new DocFile[0];
        Query query = new QueryParser(Constants.INDEX_KEY_ID, analyzer).parse("\"" + id + "\"");

        return searchResponse(searchExec(query), query);
    }

    /**
     * Search for DocFile by username
     *
     * @param username the id to search
     * @return a list if docfiles containing this username
     * @throws ParseException
     */
    public DocFile[] searchByUser(String username, String[] fileTypes) throws ParseException, IOException {
        if (indexDir.listAll().length < 2) return new DocFile[0];
        PhraseQuery  pq = new PhraseQuery (Constants.INDEX_KEY_OWNER,username);

        ArrayList<DocFile> list = new ArrayList<DocFile>();
        for (DocFile file: searchResponse(searchExec(pq),pq)) {

            if (Arrays.asList(fileTypes).contains(file.getFileType())) {
                list.add(file);
            }
        }
        DocFile[] result = new DocFile[list.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
    
    /**
     * Checks to see if a DocFile is valid. A valid DocFile is any file with the following extensions: .pdf, .txt,
     * .docx, .html
     *
     * @param file the DocFile to check validity of
     * @return boolean true if the DocFile is a valid extension
     */
    private boolean isValid (DocFile file) {
        return file != null && Arrays.asList(Constants.VALIDDOCTYPES).contains(file.getFileType());
    }

    /**
     * Commits the changes to a local copy from the RAM without shutting down the indexHandler.
     */
    public void commitWriter () {
        try {
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Commits and closes the changes to a local copy from the RAM shuts down the writer from the IndexHandler
     */
    public void closeWriter () {
        try {
            writer.close();
            indexDir.close();
            indexHandler = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a query, permission level and filetypes, return a list of matching DocFiles
     *
     * @param query a string of words
     * @param permissionLevel the permission level of the files to filter
     * @param fileTypes a list of file types, ex: [".html", ".pdf"]
     * @return a list of DocFiles matching the given params
     * @throws ParseException
     * @throws IOException 
     */
    public DocFile[] search(String query, int permissionLevel, String[] fileTypes) throws ParseException, IOException {
        if (indexDir.listAll().length < 2) return new DocFile[0];
        // create a master query builder
        BooleanQuery.Builder masterQueryBuilder = new BooleanQuery.Builder();
        // check content
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        QueryParser parser = new QueryParser(Constants.INDEX_KEY_CONTENT, analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND);
        parser.setAllowLeadingWildcard(true);
        queryBuilder.add(parser.parse(query), BooleanClause.Occur.SHOULD);
        // to match single word querries on top of all the phrases
        parser = new QueryParser(Constants.INDEX_KEY_CONTENT, analyzer);
        parser.setAllowLeadingWildcard(true);
        queryBuilder.add(parser.parse(query), BooleanClause.Occur.SHOULD);
        // check title
        parser = new QueryParser(Constants.INDEX_KEY_TITLE, analyzer);
        parser.setAllowLeadingWildcard(true);
        queryBuilder.add(parser.parse(query), BooleanClause.Occur.SHOULD);
        // check course
        parser = new QueryParser(Constants.INDEX_KEY_COURSE, analyzer);
        parser.setAllowLeadingWildcard(true);
        queryBuilder.add(parser.parse("*" + query + "*"), BooleanClause.Occur.SHOULD);
        // add to the master builder
        masterQueryBuilder.add(queryBuilder.build(), BooleanClause.Occur.MUST);
        if (permissionLevel > Constants.PERMISSION_ALL)
            masterQueryBuilder.add(new QueryParser(Constants.INDEX_KEY_PERMISSION, analyzer).parse(Integer.toString(permissionLevel)),
                    BooleanClause.Occur.MUST);

        String filterString = fileTypes[0];
        for (String fileType : fileTypes) {
            filterString += " OR " + fileType;
        }
        masterQueryBuilder.add(new QueryParser(Constants.INDEX_KEY_TYPE, analyzer).parse(filterString),
                BooleanClause.Occur.MUST);

        // build the masterQuery
        BooleanQuery masterQuery = masterQueryBuilder.build();

        return searchResponse(searchExec(masterQuery), masterQuery);
    }
    
    /**
     * Accept a list of queries and filters and return a list of DocFile that
     * matches
     *
     * @param queries     a list of String queries
     * @param filters     a list of String filters (list of Contants.INDEX_KEY*)
     * @param filterOccur if Occur.SHOULD filters will be used as additions to the search results, otherwise, if
     *                    Occur.MUST filters will further narrow down a search
     * @return a list of DocFile that matches all queries and filters
     */
    private DocFile[] search (String[] queries, String[] filters, BooleanClause.Occur filterOccur) throws ParseException {
        // create a master query builder
        BooleanQuery.Builder masterQueryBuilder = new BooleanQuery.Builder();
        // loop through all queries
        for (String query : queries) {
            if (query.equals("")) continue;
            // create a boolean query for the each query
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            // loop through all filters
            for (String filter : filters) {
                if (filter.equals("")) continue;
                QueryParser parser = new QueryParser(filter, analyzer);
                parser.setAllowLeadingWildcard(true);
                if (!filter.equals(Constants.INDEX_KEY_OWNER)) {
                    Query parsedQ = parser.parse(query);
                    queryBuilder.add(parsedQ, filterOccur);
                }
                else {
                    queryBuilder.add(new PhraseQuery(filter, queries), filterOccur);
                }
            }
            masterQueryBuilder.add(queryBuilder.build(), BooleanClause.Occur.SHOULD);
        }

        // build the masterQuery
        BooleanQuery masterQuery = masterQueryBuilder.build();

        return searchResponse(searchExec(masterQuery), masterQuery);
    }

    /**
     * Execute function for all searcher functions
     *
     * @param query is the query to search in Query format
     * @return the ScoreDoc of results
     */
    private ScoreDoc[] searchExec (Query query) {

        ScoreDoc[] hits = null;

        try {
            IndexReader reader = DirectoryReader.open(indexDir);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, hitsPerPage, new Sort(SortField.FIELD_SCORE)); //search(query, docs);
            hits = docs.scoreDocs;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hits;
    }

    /**
     * Helper function to the search methods. When provided an ScoreDoc array, this function builds the string that will
     * be returned to the user. Each search method will use this to provide a uniform returned String for all searches.
     *
     * @param results a ScoreDoc array containing the hits
     * @return a list of DocFile results of the search
     */
    public DocFile[] searchResponse(ScoreDoc[] results, Query query) {

        DocFile[] result = new DocFile[results.length];
        FastVectorHighlighter highlighter = new FastVectorHighlighter(true,true);
        FieldQuery highlightQuery = highlighter.getFieldQuery(query); 

        try {
            IndexReader reader = DirectoryReader.open(indexDir);
            IndexSearcher searcher = new IndexSearcher(reader);

            for (int i = 0; i < results.length; i++) {
                int docId = results[i].doc;
                Document document = searcher.doc(docId);
                
                //Highlight the best Content context from each Doc
                String contextString = highlighter.getBestFragment(highlightQuery, 
                        searcher.getIndexReader(), results[i].doc,Constants.INDEX_KEY_CONTENT,140);
                
                DocFile toAdd = new DocFile(
                        document.get(Constants.INDEX_KEY_FILENAME),
                        document.get(Constants.INDEX_KEY_TITLE),
                        document.get(Constants.INDEX_KEY_OWNER),
                        document.get(Constants.INDEX_KEY_PATH),
                        document.get(Constants.INDEX_KEY_STATUS).equalsIgnoreCase("true"));
                
                if (contextString != null) {
                    toAdd.setContextString(contextString);
                }
                
                toAdd.setId(document.get(Constants.INDEX_KEY_ID));
                toAdd.setPermissions(Integer.parseInt(document.get(Constants.INDEX_KEY_PERMISSION)));
                toAdd.setCourseCode(document.get(Constants.INDEX_KEY_COURSE));
                result[i] = toAdd;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public StandardAnalyzer getAnalyzer () {
        return analyzer;
    }

    public Directory getIndexDir () {
        return indexDir;
    }

    public IndexWriter getIndexWriter () {
        return writer;
    }

}
