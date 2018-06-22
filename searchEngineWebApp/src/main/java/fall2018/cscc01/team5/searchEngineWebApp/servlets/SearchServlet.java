package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;

import java.io.IOException;

public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("Show search result.");   // delete
        
        Directory index = new RAMDirectory();
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        // TODO: get query and filter
        // TODO: display result on web page, performSearch()
    }

    /* Perform searching, return search result (change void)
     * If we want more info than just file name in the search result,
     * then search result return in SearchResult type
     *
     * Assume the query and filter we get are all in string type for now
     */
    public String performSearch (String queryString, String fileType, String uploader) {
        return null;
        //TODO
    }

}