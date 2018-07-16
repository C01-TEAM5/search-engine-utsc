package fall2018.cscc01.team5.searchEngineWebApp.document;

import com.google.gson.Gson;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseDoesNotExistException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;


@WebServlet("/file")
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init () throws ServletException {
        super.init();
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        boolean get = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_GET);

        try {
            if (id == null || IndexHandler.getInstance().searchById(id.toLowerCase()).length == 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (ParseException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (get) {
            PrintWriter out = resp.getWriter();
            try {
                out.println(new Gson().toJson(IndexHandler.getInstance().searchById(id.toLowerCase())[0]));
                out.flush();
            } catch (ParseException e) {
                System.out.println("failed to get file name");
            }
            return;
        }
        RequestDispatcher view = req.getRequestDispatcher("templates/file.jsp");
        view.forward(req, resp);
    }

    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


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
