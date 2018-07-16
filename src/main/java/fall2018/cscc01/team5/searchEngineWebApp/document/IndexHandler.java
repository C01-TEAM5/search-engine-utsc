package fall2018.cscc01.team5.searchEngineWebApp.document;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class IndexHandler {

    private static IndexHandler indexHandler;

    private StandardAnalyzer analyzer;    // Use default setting
    private Directory indexDir;           // store index in Local dir
    private IndexWriterConfig config;     // Index Writer Configurations
    private IndexWriter writer;           // Index Writer
    //private String storePath;             // The path where the index will be stored
    private int hitsPerPage = 10;
    private boolean useRamDir;

    /**
     * Construct a new IndexHandler. This class represents the indexer for the search engine. Index is stored in RAM.
     */
    private IndexHandler (boolean useRamDir) throws IOException {
        analyzer = new StandardAnalyzer();
        this.useRamDir = useRamDir;
        indexDir = useRamDir ? new RAMDirectory() : FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY));
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

    /**
     * Return the shared instance of this index handler.
     *
     * @param useRamDir if true use a RAMDirectoriy instead of a FSDirectory
     * @return a shared IndexHandler
     */
    public static IndexHandler getInstance (boolean useRamDir) throws IOException {

        return new IndexHandler(useRamDir);
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
        Document newDocument = new Document();

        Field docIDField = new StringField(Constants.INDEX_KEY_ID, newFile.getId(), Store.YES);
        Field docPathField = new StringField(Constants.INDEX_KEY_PATH, newFile.getPath(), Store.YES);
        Field userIDField = new TextField(Constants.INDEX_KEY_OWNER, newFile.getOwner(), Store.YES);
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
    public void updateDoc (DocFile updatefile) {

        // Check if the file extension is valid
        if (!isValid(updatefile) || !pathExists(updatefile.getPath())) {
            return;
        }

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

        // Check if the file extension is valid
        if (!isValid(deletefile)) {
            return;
        }

        Term term = new Term(Constants.INDEX_KEY_PATH, deletefile.getPath());
        //System.out.println("delete file: " + term.field() + " " + term.text());

        // Check if path exists
        if (pathExists(deletefile.getPath())) {
            // remove doc if exits
            try {
                writer.deleteDocuments(term);
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
        return searchResponse(searchExec(new QueryParser(Constants.INDEX_KEY_ID, analyzer).parse(id))).length > 0;
    }

    /**
     * Search for DocFile by id
     *
     * @param id the id to search
     * @return a list if docfiles containing this id
     * @throws ParseException
     */
    public DocFile[] searchById(String id) throws ParseException, IOException {
        if (indexDir.listAll().length < 2) return new DocFile[0];
        return searchResponse(searchExec(new QueryParser(Constants.INDEX_KEY_ID, analyzer).parse(id)));
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
     * Accept a list of queries and filters and return a list of DocFile that matches
     *
     * @param queries        a list of String queries
     * @param filters        a list of String filters (list of Contants.INDEX_KEY*)
     * @param expandedSearch if true filters will be used as additions to the search results, otherwise filters will
     *                       further narrow down a search
     * @return a list of DocFile that matches all queries and filters
     */
    public DocFile[] search (String[] queries, String[] filters, boolean expandedSearch) throws ParseException, IOException {

        // check if there are records in the index (write.lock is always present in the directory)
        if (!useRamDir && indexDir.listAll().length < 2) return new DocFile[0];

        if (expandedSearch) return search(queries, filters, BooleanClause.Occur.SHOULD);
        else return search(queries, filters, BooleanClause.Occur.MUST);
    }

    /**
     * Given a query, permission level and filetypes, return a list of matching DocFiles
     *
     * @param query a string of words
     * @param permissionLevel the permission level of the files to filter
     * @param fileTypes a list of file types, ex: [".html", ".pdf"]
     * @return a list of DocFiles matching the given params
     * @throws ParseException
     */
    public DocFile[] search(String query, int permissionLevel, String[] fileTypes) throws ParseException {
        // create a master query builder
        BooleanQuery.Builder masterQueryBuilder = new BooleanQuery.Builder();
        // check content
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        QueryParser parser = new QueryParser(Constants.INDEX_KEY_CONTENT, analyzer);
        parser.setAllowLeadingWildcard(true);
        queryBuilder.add(parser.parse(query), BooleanClause.Occur.SHOULD);
        // check title
        parser = new QueryParser(Constants.INDEX_KEY_TITLE, analyzer);
        parser.setAllowLeadingWildcard(true);
        queryBuilder.add(parser.parse(query), BooleanClause.Occur.SHOULD);
        masterQueryBuilder.add(queryBuilder.build(), BooleanClause.Occur.MUST);
        if (permissionLevel > Constants.PERMISSION_ALL)
                masterQueryBuilder.add(IntPoint.newExactQuery(Constants.INDEX_KEY_PERMISSION, permissionLevel),
                        BooleanClause.Occur.MUST);

        String filterString = fileTypes[0];
        for (String fileType : fileTypes) {
            filterString += " OR " + fileType;
        }
        masterQueryBuilder.add(new QueryParser(Constants.INDEX_KEY_TYPE, analyzer).parse(filterString),
                BooleanClause.Occur.MUST);

        // build the masterQuery
        BooleanQuery masterQuery = masterQueryBuilder.build();

        return searchResponse(searchExec(masterQuery));
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
                Query parsedQ = parser.parse(query);
                queryBuilder.add(parsedQ, filterOccur);
            }
            masterQueryBuilder.add(queryBuilder.build(), BooleanClause.Occur.SHOULD);
        }

        // build the masterQuery
        BooleanQuery masterQuery = masterQueryBuilder.build();

        return searchResponse(searchExec(masterQuery));
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
            TopDocs docs = searcher.search(query, hitsPerPage);
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
    public DocFile[] searchResponse (ScoreDoc[] results) {

        DocFile[] result = new DocFile[results.length];

        try {
            IndexReader reader = DirectoryReader.open(indexDir);
            IndexSearcher searcher = new IndexSearcher(reader);

            for (int i = 0; i < results.length; i++) {
                int docId = results[i].doc;
                Document document = searcher.doc(docId);
                DocFile toAdd = new DocFile(
                        document.get(Constants.INDEX_KEY_FILENAME),
                        document.get(Constants.INDEX_KEY_TITLE),
                        document.get(Constants.INDEX_KEY_OWNER),
                        document.get(Constants.INDEX_KEY_PATH),
                        document.get(Constants.INDEX_KEY_STATUS).equalsIgnoreCase("true"));
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
