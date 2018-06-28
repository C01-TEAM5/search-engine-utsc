package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import com.google.gson.Gson;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        //Get query typed by user
        String query = req.getParameter("query");
        
        //Check if query is empty
        if (query.length()==0) {
        	return;
        }
        
        String[] filterList = req.getParameterValues("filters");
        StringBuilder results;
        try {
			results = performSearch(query, filterList);
	        Gson gson = new Gson();
	        resp.setContentType("application/json");
	        String jsonResponse = gson.toJson(results);
	        
	        PrintWriter output = resp.getWriter();
	        output.print(jsonResponse);
	        output.flush();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    /* Perform searching, return search result (change void)
     * If we want more info than just file name in the search result,
     * then search result return in SearchResult type
     *
     * Assume the query and filter we get are all in string type for now
     */
    public StringBuilder performSearch (String queryString, String[] filterString) throws ParseException {
    	
    	ArrayList<String> searchFilters = new ArrayList<String>();
    	ArrayList<String> searchQuery = new ArrayList<String>();
    	searchQuery.add(queryString);
        filterDecoder(filterString, searchQuery, searchFilters);
        
        IndexHandler handler = IndexHandler.getInstance();
        String [] queryList = new String[searchQuery.size()];
        queryList = searchQuery.toArray(queryList);
        String [] filterList = new String[searchFilters.size()];
        filterList = searchQuery.toArray(filterList);
        
        DocFile[] docFileResults;
		
		docFileResults = handler.search(queryList, filterList, true);
		
        StringBuilder results = new StringBuilder("Found: ");
        
        for (DocFile result:docFileResults) {
        	results.append(result);
        }
        
		return results;
    	
    }
    	
    private static void filterDecoder(String[] fString, ArrayList<String> searchFilters, ArrayList<String> sQuery) {
    		
       	searchFilters.add("Content");
        	
       	for (String filterType:fString) {
       		if (filterType.equals("ePdf")) {
       			sQuery.add(".pdf");
       			if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) {
       				searchFilters.add(Constants.INDEX_KEY_TYPE);
       			}
       		} else if (filterType.equals("eTxt")){
       			sQuery.add(".txt");
       			if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) {
       				searchFilters.add(Constants.INDEX_KEY_TYPE);
       			}
       		} else if (filterType.equals("eHtml")) {
       			sQuery.add(".html");
       			if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) {
       				searchFilters.add(Constants.INDEX_KEY_TYPE);
       			}
       		} else if (filterType.equals("eDocx")) {
       			sQuery.add(".docx");
       			if (!searchFilters.contains(Constants.INDEX_KEY_TYPE)) {
       				searchFilters.add(Constants.INDEX_KEY_TYPE);
       			}
       		}
       	}
    	
    	
    }

}