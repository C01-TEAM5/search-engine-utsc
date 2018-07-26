package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;

import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.routines.UrlValidator;

public class CrawlerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {
        
        String currentUser = getCurrentUser(req.getCookies());
        String courseId = req.getParameter("crawlerCourseCode");
        String crawlSite = req.getParameter("crawlSite");
        UrlValidator validator = new UrlValidator();
        
        //Check for valid website input
        if (!validator.isValid(crawlSite)) {
            System.out.println("Invalid URL");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        System.out.println("Received POST Request");
        System.out.println("User is "+currentUser+" and course is "+courseId);
        System.out.println("URL to crawl is: "+crawlSite);
        
        
        
        resp.sendRedirect("/upload");
        
    }
   
    
    private String getCurrentUser(Cookie[] cookies) {
        String res = "";
        if (cookies == null) return res;
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("currentUser")) res = cookie.getValue();
        }

        return res;
    }
}

