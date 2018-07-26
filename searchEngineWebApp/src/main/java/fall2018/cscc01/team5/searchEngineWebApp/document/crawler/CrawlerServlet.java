package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class CrawlerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {
        
        String currentUser = getCurrentUser(req.getCookies());
        String courseId = req.getParameter("crawlerCourseCode");
        String crawlSite = req.getParameter("crawlSite");
        System.out.println("Received POST Request");
        System.out.println("User is "+currentUser+" and course is "+courseId);
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

