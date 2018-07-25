package fall2018.cscc01.team5.searchEngineWebApp.document;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import fall2018.cscc01.team5.searchEngineWebApp.course.Course;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseDoesNotExistException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;


@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private boolean isMultipart;
    private int maxSize = 25 * 1024 * 1024;

    @Override
    public void init () throws ServletException {
        super.init();
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //throw new ServletException("GET method used with " + getClass().getName()+": POST method required.");
        RequestDispatcher view = req.getRequestDispatcher("upload.html");
        view.forward(req, resp);
    }

    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {

        String currentUser = getCurrentUser(req.getCookies());
        String courseId = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        if (!currentUser.equals("") && AccountManager.exists(currentUser)) {
            // check upload request
            isMultipart = ServletFileUpload.isMultipartContent(req);
            resp.setContentType("text/html");
            java.io.PrintWriter out = resp.getWriter();

            // empty return
            if (!isMultipart) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }


            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(maxSize); // maximum file size to be uploaded.

            try {
              
                // parse multiple files
                List items = upload.parseRequest(req);
                Iterator itemIterator = items.iterator();

                if (!itemIterator.hasNext()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                
                // get course code as the last element of list
                String courseCode = "";
                if (items.size()> 0) {
                  FileItem lastItem = (FileItem) items.get(items.size() - 1);
                  if (lastItem.isFormField()) {
                    courseCode = lastItem.getString();
                  }
                }

                while (itemIterator.hasNext()) {
                    FileItem item = (FileItem) itemIterator.next();
                    if (!item.isFormField()) {
                        // gets file data
                        String fileName = item.getName();
                        String filePath = Uploader.getUploadPath(currentUser);
                        
                        //Get file InputStream
                        InputStream initialStream = item.getInputStream();

                        //Set up DocFile to uploaded
                        DocFile docFile = new DocFile(fileName, fileName, currentUser, filePath + fileName, true);
                        docFile.setPermissions(AccountManager.getPermission(currentUser));
                        
                        //Set course information if it exists (courseCode comes from upload form, courseID from course page)
                        if (courseId != null) {
                            courseId = courseId.toLowerCase();
                            docFile.setCourseCode(courseId);
                                
                        }
                        else if (courseCode != "") {
                            courseCode = courseCode.toLowerCase();
                            docFile.setCourseCode(courseCode);
                        }
                        
                        //Upload the file and index it
                        Uploader.handleUpload(docFile, initialStream);
                            
                    }
                }

            } catch (CourseDoesNotExistException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("adding course failed");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (courseId != null && CourseManager.courseExists(courseId.toLowerCase())) {
                PrintWriter output = resp.getWriter();
                try {
                    output.print(new Gson().toJson(CourseManager.getCourse(courseId).getAllFiles()));
                } catch (CourseDoesNotExistException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    output.flush();
                    return;
                }
                output.flush();
                return;
            }
            resp.sendRedirect("/upload");
        }
        else {
            if (courseId != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            resp.sendRedirect("/upload?error");
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
