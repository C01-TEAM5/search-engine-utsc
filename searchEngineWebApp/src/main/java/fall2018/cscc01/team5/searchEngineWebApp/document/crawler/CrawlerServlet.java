package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.validator.routines.UrlValidator;

import fall2018.cscc01.team5.searchEngineWebApp.course.CourseManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.ServletUtil;

public class CrawlerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {
        
        String currentUser = "";
        try {
            currentUser = ServletUtil.getDecodedCookie(req.getCookies());
        } catch (InvalidKeySpecException e1) {
            resp.sendRedirect("/upload?error");
            return;
        } 
        catch (NoSuchAlgorithmException e1) {
            resp.sendRedirect("/upload?error");
            return;
        } 
        catch (DecoderException e1) {
            resp.sendRedirect("/upload?error");
            return;
        }
        
        String courseId = req.getParameter("crawlerCourseCode");
        String crawlSite = req.getParameter("crawlSite");
        UrlValidator validator = new UrlValidator();       
        
        //Check for valid website input
        if (!validator.isValid(crawlSite)) {
            resp.sendRedirect("/upload?error");
            return;
            //resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        //Check for valid Course input
        if (courseId != "" && !CourseManager.courseExists(courseId)) {
            resp.sendRedirect("/upload?error");
            return;
            //resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        //Crawl the website
        try {
            CrawlerController controller = new CrawlerController(crawlSite, 2, 40, currentUser, courseId);
            controller.startCrawl();
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        resp.sendRedirect("/upload");
        
    }
   
}

