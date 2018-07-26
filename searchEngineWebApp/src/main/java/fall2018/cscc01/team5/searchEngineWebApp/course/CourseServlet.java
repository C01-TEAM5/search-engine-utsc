package fall2018.cscc01.team5.searchEngineWebApp.course;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.result.UpdateResult;
import fall2018.cscc01.team5.searchEngineWebApp.document.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import fall2018.cscc01.team5.searchEngineWebApp.util.ServletUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.print.DocFlavor;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DocumentFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CourseServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String currentUser = null;
        try {
            currentUser = ServletUtil.getDecodedCookie(req.getCookies());
        }
        catch (InvalidKeySpecException e) {}
        catch (NoSuchAlgorithmException e) {}
        catch (DecoderException e) {}
        catch (Exception e) {}

        String courseID = req.getParameter(Constants.SERVLET_PARAMETER_ID);

        if (courseID == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            Gson gson = new Gson();

            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = req.getReader().readLine()) != null) {
                sb.append(s);
            }

            Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {
            }.getType());
            String addStudent = map.get("addStudnet");
            String addInstructor = map.get("addInstructor");
            String addFile = map.get("addFile");
            String removeStudent = map.get("removeStudent");
            String removeInstructor = map.get("removeInstructor");
            String removeFile = map.get("removeFile");
            String newCode = map.get("courseCode");
            String newName = map.get("courseName");
            String newDesc = map.get("courseDesc");
            String newSize = map.get("courseSize");

            try {
                courseID = courseID.toLowerCase();
                Course c = CourseManager.getCourse(courseID);

                if (currentUser == null || !c.getAllInstructors().contains(currentUser)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                if (addStudent != null) {
                    if (AccountManager.exists(addStudent)) {
                        User u = AccountManager.getUser(addStudent.toLowerCase());
                        u.enrollInCourse(courseID.toLowerCase());
                        if (u.getPermission() != Constants.PERMISSION_STUDENT) {
                            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an student");
                            return;
                        }
                        AccountManager.updateUser(u.getUsername().toLowerCase(), u);
                        c.addStudent(addStudent.toLowerCase());
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (removeStudent != null) {
                    if (!c.removeStudent(removeStudent)) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    else {
                        User u = AccountManager.getUser(removeStudent.toLowerCase());
                        u.dropCourse(courseID.toLowerCase());
                        AccountManager.updateUser(u.getUsername().toLowerCase(), u);
                    }
                }

                if (addInstructor != null) {
                    if (AccountManager.exists(addInstructor)) {
                        User u = AccountManager.getUser(addInstructor.toLowerCase());
                        u.enrollInCourse(courseID.toLowerCase());
                        if (u.getPermission() != Constants.PERMISSION_INSTRUCTOR) {
                            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an instructor");
                            return;
                        }
                        AccountManager.updateUser(u.getUsername().toLowerCase(), u);
                        c.addInstructor(addInstructor.toLowerCase());
                        c.removeInstructor(addInstructor.toLowerCase());
                        c.removeStudent(addInstructor.toLowerCase());
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (removeInstructor != null) {
                    if (!c.removeInstructor(removeInstructor)) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    else {
                        User u = AccountManager.getUser(removeInstructor.toLowerCase());
                        u.dropCourse(courseID.toLowerCase());
                        AccountManager.updateUser(u.getUsername().toLowerCase(), u);
                    }
                }

                if (addFile != null) {
                    if (IndexHandler.getInstance().fileExists(addFile)) {
                        c.addFile(addFile);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (removeFile != null) {
                    for (DocFile file: IndexHandler.getInstance().searchById(removeFile, new String[]{"html", "pdf", "txt", "docx"})) {
                        file.setCourseCode("");
                        IndexHandler.getInstance().updateDoc(file);
                    }

                    if (!c.removeFile(removeFile)) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }

                if (newCode != null) {
                    for (String userId : c.getAllInstructors()) {
                        User user = AccountManager.getUser(userId.toLowerCase());
                        System.out.println("\n\n\n" + user.getUsername() + "\nUpdating courses\n\n" + courseID + "\n\n\n" + user.getCourses().toString() + "\n\n");
                        user.dropCourse(courseID.toLowerCase());
                        user.enrollInCourse(newCode.toLowerCase());
                        AccountManager.updateUser(user.getUsername().toLowerCase(), user);
                    }

                    for (String userId : c.getAllStudents()) {
                        User user = AccountManager.getUser(userId.toLowerCase());
                        user.dropCourse(courseID.toLowerCase());
                        user.enrollInCourse(newCode.toLowerCase());
                        AccountManager.updateUser(user.getUsername().toLowerCase(), user);
                    }

                    for (String userId : c.getAllTAs()) {
                        User user = AccountManager.getUser(userId.toLowerCase());
                        user.dropCourse(courseID.toLowerCase());
                        user.enrollInCourse(newCode.toLowerCase());
                        AccountManager.updateUser(user.getUsername().toLowerCase(), user);
                    }

                    c.setCode(newCode.toLowerCase());
                }

                if (newName != null) {
                    c.setName(newName);
                }

                if (newDesc != null) {
                    c.setDescription(newDesc);
                }

                if (newSize != null) {
                    c.setSize(Integer.parseInt(newSize));
                }

                UpdateResult res = CourseManager.updateCourse(courseID, c);
                if (res != null) {
                    PrintWriter output = resp.getWriter();
                    output.print(new Gson().toJson(c));
                    output.flush();
                    return;
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            } catch (CourseDoesNotExistException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            } catch (ParseException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            } catch (NumberFormatException e) {
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

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        //throw new ServletException("GET method used with " + getClass().getName()+": POST method required.");
        String courseID = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        boolean getStudents = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_COURSE_GET_STUDENT);
        boolean getInstructorus = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_COURSE_GET_INSTRUCTORS);
        boolean getFiles = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_COURSE_GET_FILES);
        boolean getTAs = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_COURSE_GET_TAS);
        boolean get = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_GET);

        if (courseID == null) {
            System.out.println("course ID is null: " + courseID);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (courseID.equals("")) {
            System.out.println("course ID is empty: " + courseID);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            try {
                Course course = CourseManager.getCourse(courseID.toLowerCase());
                PrintWriter output = resp.getWriter();

                if (getStudents) {
                    output.print(new Gson().toJson(course.getAllStudents()));
                    output.flush();
                    return;
                }

                if (getInstructorus) {
                    output.print(new Gson().toJson(course.getAllInstructors()));
                    output.flush();
                    return;
                }

                if (getFiles) {
                    output.print(new Gson().toJson(course.getAllFiles()));
                    output.flush();
                    return;
                }

                if (getTAs) {
                    output.print(new Gson().toJson(course.getAllTAs()));
                    output.flush();
                    return;
                }

                if (get) {
                    output.print(new Gson().toJson(course));
                    output.flush();
                    return;
                }

                req.setAttribute("courseID", courseID.toUpperCase());
                req.setAttribute("courseName", course.getName());
                req.setAttribute("courseDesc", course.getDescription());
                req.setAttribute("numStudentsEnrolled", Integer.toString(course.getSize()));
                RequestDispatcher view = req.getRequestDispatcher("templates/course.jsp");
                view.forward(req, resp);
            } catch (CourseDoesNotExistException e) {
                System.out.println("course does not exist: " + courseID);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }


    }
}
