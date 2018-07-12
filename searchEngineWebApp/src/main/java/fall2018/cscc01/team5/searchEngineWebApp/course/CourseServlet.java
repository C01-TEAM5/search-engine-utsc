package fall2018.cscc01.team5.searchEngineWebApp.course;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

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
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {

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
            req.setAttribute("courseID", courseID.toUpperCase());
            req.setAttribute("instID", getCurrentUser(req.getCookies()));
            req.setAttribute("courseName", "Computer Science I");
            req.setAttribute("courseDesc", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).");
            req.setAttribute("courseInstructor", "Crazy Bob");
            req.setAttribute("numStudentsEnrolled", "485");
            RequestDispatcher view = req.getRequestDispatcher("templates/course.jsp");
            view.forward(req, resp);
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
