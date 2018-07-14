package fall2018.cscc01.team5.searchEngineWebApp.course;

import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourseServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String courseID = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_ID); 
        String addStudent = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_ADD_STUDENT);
        String removeStudent = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_REMOVE_STUDENT);
        String addInstructor = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_ADD_INSTRUCTOR);
        String removeInstructor = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_REMOVE_INSTRUCTOR);
        String addFile = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_ADD_FILE);
        String removeFile = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_REMOVE_FILE);


        if (courseID == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            try {
                Course c = CourseManager.getCourse(courseID);
                if (addStudent != null) {
                    if (AccountManager.exists(addStudent)) {
                        c.addStudent(addStudent);
                    }
                    else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }
                
                if (removeStudent != null) {
                    if (!c.removeStudent(removeStudent)) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }
                
                if (addInstructor != null) {
                    if (AccountManager.exists(addInstructor)) {
                        c.addInstructor(addInstructor);
                    }
                    else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (removeInstructor != null) {
                    if (!c.removeInstructor(removeInstructor)) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (addFile != null) {
                    if (IndexHandler.getInstance().fileExists(addFile)) {
                        c.addFile(addFile);
                    }
                    else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (removeFile != null) {
                    if (!c.removeFile(removeFile)) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }
            } catch (CourseDoesNotExistException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            } catch (ParseException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

        }
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //throw new ServletException("GET method used with " + getClass().getName()+": POST method required.");
        String courseID = req.getParameter(Constants.SERVLET_PARAMETER_COURSE_ID); 

        if (courseID == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else if (courseID.equals("")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            try {
                Course course = CourseManager.getCourse(courseID.toLowerCase());
                req.setAttribute("courseID", courseID.toUpperCase());
                req.setAttribute("courseName", course.getName());
                req.setAttribute("courseDesc", course.getDescription());
                req.setAttribute("numStudentsEnrolled", Integer.toString(course.getSize()));
                RequestDispatcher view = req.getRequestDispatcher("templates/course.jsp");
                view.forward(req, resp);
            } catch (CourseDoesNotExistException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        
        
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
