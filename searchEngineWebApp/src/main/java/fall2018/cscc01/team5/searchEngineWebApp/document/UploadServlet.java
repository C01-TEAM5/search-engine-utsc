package fall2018.cscc01.team5.searchEngineWebApp.document;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import fall2018.cscc01.team5.searchEngineWebApp.util.ServletUtil;
import org.apache.commons.codec.DecoderException;
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

        String currentUser = null;
        try {
            currentUser = ServletUtil.getDecodedCookie(req.getCookies());
        }
        catch (InvalidKeySpecException e) {}
        catch (NoSuchAlgorithmException e) {}
        catch (DecoderException e) {}
        catch (Exception e) {}

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
            //factory.setSizeThreshold(maxMemSize); // maximum size that will be stored in memory
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
                        String filePath = Constants.FILE_PUBLIC_PATH + currentUser + File.separator;
                        String contentType = item.getContentType();
                        boolean isInMemory = item.isInMemory();
                        long sizeInBytes = item.getSize();

                        
                        // creates the save directory if it does not exists
                        File fileSaveDir = new File(filePath);
                        if (!fileSaveDir.exists()) {
                            fileSaveDir.mkdir();
                        }


                        File targetFile = new File(filePath + fileName);
                        //Only upload the file if it does not exist already
                        if (!targetFile.isFile()) {
                            InputStream initialStream = item.getInputStream();

                            // writes data to indexHandler
                            DocFile docFile = new DocFile(fileName, fileName, currentUser, filePath + fileName, true);
                            docFile.setPermissions(AccountManager.getPermission(currentUser));
                            
                            if (courseId != null ) {
                                if (!CourseManager.courseExists(courseId.toLowerCase())){
                                    resp.sendRedirect("/upload?error");
                                    return;
                                }
                                courseId = courseId.toLowerCase();
                                docFile.setCourseCode(courseId);
                                String fileId = FileManager.upload(fileName, currentUser, true, fileName, docFile.getFileType(), 
                                        docFile.getPermission(), courseId, docFile.getId(), initialStream);
                                docFile.setId(fileId);
                                Course c = CourseManager.getCourse(courseId);
                                c.addFile(docFile.getId());
                                CourseManager.updateCourse(courseId, c);
                                
                            }
                            else if (courseCode!= null) {
                                if (courseCode != "" && !CourseManager.courseExists(courseCode.toLowerCase())){
                                    resp.sendRedirect("/upload?error");
                                    return;
                                }
                                courseCode = courseCode.toLowerCase();
                                docFile.setCourseCode(courseCode);
                                String fileId = FileManager.upload(fileName, currentUser, true, fileName, docFile.getFileType(), 
                                        docFile.getPermission(), courseCode, docFile.getId(), initialStream);
                                docFile.setId(fileId);
                                Course c = CourseManager.getCourse(courseCode);
                                c.addFile(docFile.getId());
                                CourseManager.updateCourse(courseCode, c);
                            }
                            else {
                                String fileId = FileManager.upload(fileName, currentUser, true, fileName, docFile.getFileType(), 
                                        docFile.getPermission(), docFile.getCourseCode(), docFile.getId(), initialStream);
                                docFile.setId(fileId);
                            }
                            
                            docFile.setPath(FileManager.download(docFile.getId(), docFile.getFileType()));
                            IndexHandler indexHandler = IndexHandler.getInstance();
                            indexHandler.addDoc(docFile);
                            // inform all subscribers
                            User user = AccountManager.getUser(currentUser);
                            for (String id: user.getFollowers()) {
                                User toInform = AccountManager.getUser(id);
                                String msg = constructMsg(user, toInform, docFile);
                                AccountManager.sendNotification(toInform, msg);
                            }
                        }
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
//            PrintWriter output = resp.getWriter();
//            output.print(new Gson().toJson("Success"));
//            output.flush();
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

    private String constructMsg(User from, User to, DocFile docFile) {

        StringBuilder res = new StringBuilder();
        res.append("Hello " + to.getName() + ",");
        res.append("\n");
        res.append("\n");
        res.append("One of your followers, " + from.getName() + ", has just uploaded a new file, " + docFile.getFilename());
        res.append("\n");
        res.append("\n");
        res.append("Sincerely,");
        res.append("\n");
        res.append("Search Engine UTSC Team");

        return res.toString();

    }
}
