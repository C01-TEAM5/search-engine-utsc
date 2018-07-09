package fall2018.cscc01.team5.searchEngineWebApp.document;

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
     */
    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
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
        
        Gson gson = new Gson();
        resp.setContentType("application/json");
       
        try {
            //Write the response
	        String searchResults = performSearch(searchQuery,filterQuery);
            String jsonResponse = gson.toJson(searchResults);
	        PrintWriter output = resp.getWriter();
	        output.print(jsonResponse);
	        output.flush();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    /**
     * Helper method to print the results of the search.
     * Returns a string to be returned to the front end.
     * 
     * @param resultFiles the DocFile array of results
     * @return String the String to be returned to the user
     */
    private String printResults(DocFile[] resultFiles) {
		
    	if (resultFiles.length==0) {
    		return "No results found.";
    	}
    	
    	StringBuilder results = new StringBuilder();
    	
    	if (resultFiles.length==1) {
    	    results.append("1 result found.\n");
    	} else {
    	    results.append(resultFiles.length + " results found.\n");
    	}
    	
    	for (DocFile result:resultFiles) {
    	    results.append(result.toString()+"\n\n");
    	}
    	
    	return results.toString();
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
    public String performSearch (String[] queryString, String[] filterString) throws ParseException, IOException {
    	
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
		String results = printResults(docFileResults);
        
		return results;
    	
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