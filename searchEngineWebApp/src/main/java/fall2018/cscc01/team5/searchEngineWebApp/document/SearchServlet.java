package fall2018.cscc01.team5.searchEngineWebApp.document;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import com.google.gson.Gson;

import fall2018.cscc01.team5.searchEngineWebApp.document.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Current implementation:
     * If there are no filters (no boxes checked), only content is searched with the appropriate query.
     * If a document type has been checked, all content is searched as well as 
     * all documents of the checked document type. (This will be changed down the road.)
     * 
     * Any user can perform a search (even if they do not have an account)
     * 
     * Learned pagination from: https://stackoverflow.com/questions/31410007/how-to-do-pagination-in-jsp
     * 
     */
    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        int currentPage = 1; //current page we are on
        int resultsPerPage = 4; //number of results to be shown per page
        int totalResults; //total search results on all pages
        int pagesRequired; //pages required to display total search results
        
        String noPageUri = removePageQuery(req.getQueryString());
        
        //adjust current page if not on page 1
        if(req.getParameter("page") != null) {
            currentPage = Integer.parseInt(req.getParameter("page"));
        }
        
        //Get query typed by user
        String query = req.getParameter("query");
        if (query.length()==0) {
            return;
        }
        String[] searchQuery = query.split(" ");
        
        //Get parameters for search. If none, search all types of docs.
        String filterParam = req.getParameter("filters");
        if (filterParam==null) {
            filterParam = "";
        }        
        String[] filterQuery = filterParam.split(",");
        
        //Perform search, only send the results we want shown on page to jsp
        try {
            DocFile[] searchResults = performSearch(searchQuery,filterQuery);
            totalResults = searchResults.length;
            pagesRequired = (int) Math.ceil(totalResults/resultsPerPage);
            
            //Set the start index we want to show, if an invalid page was
            //entered into the URL, just go to the first one
            int startIndex = (currentPage-1)*resultsPerPage;          
            if (startIndex > totalResults) {
                startIndex = 0;
            }
            
            //Only show up to the number of results we have
            int endIndex = startIndex+resultsPerPage;
            if (endIndex > totalResults) {
                endIndex = totalResults;
            }
            
            DocFile[] pageResults = Arrays.copyOfRange(searchResults, 
                    startIndex, endIndex);
            req.setAttribute("searchResults", pageResults);
            req.setAttribute("totalResults", totalResults);
            req.setAttribute("totalPages", pagesRequired);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("noPageUri", noPageUri);
            req.setAttribute("minPageDisplay", pageDisplay(currentPage, pagesRequired)[0]);
            req.setAttribute("maxPageDisplay", pageDisplay(currentPage, pagesRequired)[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    
        RequestDispatcher viewResults = req.getRequestDispatcher("/WEB-INF/searchresults.jsp");
        viewResults.forward(req, resp);
        
    }
    
    /**
     * Calculates the minimum page number to display at the bottom
     * of the jsp page.
     * 
     * @param currentPage
     * @return
     */
    private int[] pageDisplay(int currentPage, int pagesRequired) {
        
        int pageOnEachSide = 3;
        int minPage = currentPage - pageOnEachSide;
        int maxPage = currentPage + pageOnEachSide;
        
        if (minPage < 1) {
            maxPage += Math.abs(minPage-1);
            minPage = 1;
        }
               
        if (maxPage > pagesRequired) {
            maxPage = pagesRequired;
        }
        
        int[] returnArray = {minPage, maxPage};
        
        return returnArray;
        
    }
    
    /**
     * Helper function to remove the page parameter from the URI.
     * 
     * @param query
     * @return
     */
    private String removePageQuery(String query) {
        
        String[] queryArray = query.split("&");
        StringBuilder returnUri = new StringBuilder("/search?");
        
        for(String param:queryArray) {
            if (!param.contains("page=")) {
                returnUri.append(param+"&");
            }
        }
                
        return returnUri.toString();
        
    }
    
    /**
     * Helper method to perform the search. Takes in
     * the array of queries passed in by the user and the raw filter array
     * sent to the Servlet. Processes the raw filters, performs the search
     * and returns the result in a String format.
     * 
     * @param queryString String array of queries sent by the user
     * @param filterString Raw filter String array sent to the servlet
     * @return String results to be sent to the user
     * @throws ParseException
     * @throws IOException
     */
    public DocFile[] performSearch (String[] queryString, String[] filterString) throws ParseException, IOException {
    	
    	ArrayList<String> searchFilters = new ArrayList<String>();
    	ArrayList<String> searchQuery = new ArrayList<String>(Arrays.asList(queryString));
    	
        filterDecoder(filterString, searchQuery, searchFilters);
        
        IndexHandler handler = IndexHandler.getInstance();
        String [] queryList = new String[searchQuery.size()];
        queryList = searchQuery.toArray(queryList);
        String [] filterList = new String[searchFilters.size()];
        filterList = searchFilters.toArray(filterList);        
        
        DocFile[] docFileResults = handler.search(queryList, filterList, true);
		handler.closeWriter();
		//String results = printResults(docFileResults);
        
		return docFileResults;
    	
    }
    
    /**
     * Helper function to decode the raw filter array and adjust the searchQuery as necessary.
     * Current implementation:
     * If there are no filters, only content is searched with the appropriate query.
     * If a document type has been checked, all content is searched as well as 
     * all documents of the checked document type. (This will be changed down the road.)
     * 
     * @param filterString the raw filter String array
     * @param searchQuery search query sent by the user
     * @param searchFilters the ArrayList that will hold the actual filters sent to search
     */
    private static void filterDecoder(String[] filterString, ArrayList<String> searchQuery, ArrayList<String> searchFilters) {

        searchFilters.add("Content");
        
        for (String filter:filterString) {
            if (filter.equals("ePdf")) {
                searchQuery.add(".pdf");
                if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) searchFilters.add(Constants.INDEX_KEY_TYPE);
            } else if (filter.equals("eTxt")) {
                searchQuery.add(".txt");
                if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) searchFilters.add(Constants.INDEX_KEY_TYPE);
            } else if (filter.equals("eHtml")) {
                searchQuery.add(".html");
                if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) searchFilters.add(Constants.INDEX_KEY_TYPE);
            } else if (filter.equals("eDocx")) {
                searchQuery.add(".docx");
                if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) searchFilters.add(Constants.INDEX_KEY_TYPE);
            }
        }
        
    }

}