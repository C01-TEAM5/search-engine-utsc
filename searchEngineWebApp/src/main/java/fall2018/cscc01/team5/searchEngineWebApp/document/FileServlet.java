package fall2018.cscc01.team5.searchEngineWebApp.document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseDoesNotExistException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.bson.types.ObjectId;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@WebServlet("/file")
public class FileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        boolean get = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_GET);

        try {
            if (id == null || IndexHandler.getInstance().searchById(id.toLowerCase(), new String[]{"html", "pdf", "txt", "docx"}).length == 0) {
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
                out.println(new Gson().toJson(IndexHandler.getInstance().searchById(id.toLowerCase(), new String[]{"html", "pdf", "txt", "docx"})[0]));
                out.flush();
            } catch (ParseException e) {
                System.out.println("failed to get file name");
            }
            return;
        }

        try {
            DocFile file = IndexHandler.getInstance().searchById(id.toLowerCase(), new String[]{"html", "pdf", "txt", "docx"})[0];
            String path = "https://s3.amazonaws.com/search-engine-utsc/" + file.getId() + "." + file.getFileType();
            req.setAttribute("path", path);
            req.setAttribute("fileName", file.getTitle());
            req.setAttribute("courseId", file.getCourseCode());
            req.setAttribute("permission", file.getPermission());
            req.setAttribute("owner", file.getOwner());
            req.setAttribute("fileType", file.getFileType());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RequestDispatcher view = req.getRequestDispatcher("templates/file.jsp");
        view.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        String currentUser = getCurrentUser(req.getCookies());

        if (currentUser == null || currentUser == "") {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DocFile file = null;

        try {
            file = IndexHandler.getInstance().searchById(id, new String[]{"html", "pdf", "txt", "docx"})[0];
            if (!FileManager.fileExists(id, file.getFileType())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "File does not exist");
                return;
            }
        } catch (ParseException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "File does not exist");
            return;
        }

        if (!file.getOwner().equals(currentUser)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid owner");
            return;
        }

        Gson gson = new Gson();

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }

        Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
        String newName = map.get("name");
        String newCourse = map.get("course");
        String newPerm = map.get("permission");

        if (newName.equalsIgnoreCase("") || newPerm.equalsIgnoreCase("")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid params");
            return;
        }

        if (!newCourse.equals("") && !CourseManager.courseExists(newCourse)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course");
            return;
        }

        file.setTitle(newName);
        file.setCourseCode(newCourse);
        try {
            file.setPermissions(Integer.parseInt(newPerm));
        }
        catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid params");
            return;
        }

        String newId = FileManager.update(id, file);
        try {
            file.setId(newId);
            IndexHandler.getInstance().updateDoc(file);
        } catch (ParseException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not update");
            return;
        }


        PrintWriter output = resp.getWriter();
        output.print(new Gson().toJson(newId));
        output.flush();
    }

    private String getCurrentUser(Cookie[] cookies) {
        String res = "";
        if (cookies == null) return res;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("currentUser")) res = cookie.getValue();
        }

        return res;
    }
}
