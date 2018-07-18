package fall2018.cscc01.team5.searchEngineWebApp.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fall2018.cscc01.team5.searchEngineWebApp.course.Course;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseDoesNotExistException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseManager;
import fall2018.cscc01.team5.searchEngineWebApp.document.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.document.FileManager;
import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class ProfileServlet extends HttpServlet {

    public ProfileServlet() {
        super();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        boolean createCourse = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_CREATE_COURSE);
        boolean deleteFile = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_DELETE_FILE);
        String currentUser = getCurrentUser(req.getCookies());

        if (currentUser == null || currentUser == "") {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Gson gson = new Gson();

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }

        Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
        String newId = map.get("id");
        String newName = map.get("name");
        String newDesc = map.get("desc");
        String newImage = map.get("image");
        String courseId = map.get("courseId");
        String fileId = map.get("fileId");

        if (createCourse) {
            try {
                User user = AccountManager.getUser(currentUser);
                if (user.getPermission() != Constants.PERMISSION_INSTRUCTOR) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                Course course = new Course("", courseId, 0, new String[]{currentUser});
                CourseManager.addCourse(course);
                user.enrollInCourse(courseId);
                AccountManager.updateUser(currentUser, user);
            } catch (InvalidUsernameException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (CourseAlreadyExistsException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (EmailAlreadyExistsException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (UsernameAlreadyExistsException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PrintWriter output = resp.getWriter();
            output.print(new Gson().toJson(courseId));
            output.flush();
            return;
        }
        else if (deleteFile) {
            try {
                User user = AccountManager.getUser(currentUser);
                if (!IndexHandler.getInstance().fileExists(fileId)) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                DocFile file = IndexHandler.getInstance().searchById(fileId)[0];
                file.setId(fileId);
                if (CourseManager.courseExists(file.getCourseCode())) {
                    Course course = CourseManager.getCourse(file.getCourseCode());
                    course.removeFile(fileId);
                    CourseManager.updateCourse(course.getCode(), course);
                }
                IndexHandler.getInstance().removeDoc(file);
                AccountManager.updateUser(currentUser, user);
                FileManager.deleteFile(file.getId());
            } catch (EmailAlreadyExistsException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (UsernameAlreadyExistsException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (ParseException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (CourseDoesNotExistException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (InvalidUsernameException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            PrintWriter output = resp.getWriter();
            output.print(new Gson().toJson(courseId));
            output.flush();
            return;
        }

        try {
            User user = AccountManager.getUser(currentUser);
            User newUser = new User(newId, user.getEmail(), newName, "");
            newUser.setHash(user.getHash());
            newUser.setDescription(newDesc);
            newUser.setCourses(user.getCourses());
            newUser.setPermissions(user.getPermission());

            AccountManager.updateUser(user.getUsername(), newUser);
            PrintWriter output = resp.getWriter();
            output.print(new Gson().toJson("Success"));
            output.flush();
        } catch (InvalidUsernameException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (EmailAlreadyExistsException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        boolean get = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_GET);
        String currentUser = getCurrentUser(req.getCookies());

        if (id == null || !AccountManager.exists(id.toLowerCase())) {
            if (currentUser == null || currentUser.equals("")) {
                resp.sendRedirect("/");
                return;
            }
            else {
                resp.sendRedirect("/profile?id="+currentUser);
                return;
            }
        }

        if (get) {
            PrintWriter out = resp.getWriter();
            try {
                out.println(new Gson().toJson(AccountManager.getUser(id.toLowerCase())));
                out.flush();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidUsernameException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            User user = AccountManager.getUser(currentUser);
            DocFile[] files = IndexHandler.getInstance().searchByUser(user.getUsername());
            req.setAttribute("userId", user.getUsername());
            req.setAttribute("name", user.getName());
            req.setAttribute("desc", user.getDescription());
            req.setAttribute("numOfFiles", files.length);
            req.setAttribute("files", files);
            req.setAttribute("permission", user.getPermission());
            req.setAttribute("courses", user.getCourses());

            RequestDispatcher view = req.getRequestDispatcher("templates/profile.jsp");
            view.forward(req, resp);
        } catch (InvalidUsernameException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (ParseException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
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
