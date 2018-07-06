package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;

import org.apache.commons.io.FileUtils;


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
    }

    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {

        // check upload request
        isMultipart = ServletFileUpload.isMultipartContent(req);
        resp.setContentType("text/html");
        java.io.PrintWriter out = resp.getWriter();

        // empty return
        if (!isMultipart) {
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

            while (itemIterator.hasNext()) {
                FileItem item = (FileItem) itemIterator.next();

                if (!item.isFormField()) {

                    // gets file data
                    String fileName = item.getName();
                    String filePath = Constants.FILE_UPLOAD_PATH;
                    String contentType = item.getContentType();
                    boolean isInMemory = item.isInMemory();
                    long sizeInBytes = item.getSize();

                    // creates the save directory if it does not exists
                    File fileSaveDir = new File(filePath);
                    if (!fileSaveDir.exists()) {
                        fileSaveDir.mkdir();
                    }

                    InputStream initialStream = item.getInputStream();
                    File targetFile = new File(filePath + fileName);

                    FileUtils.copyInputStreamToFile(initialStream, targetFile);

                    // writes data to indexHandler
                    DocFile docFile = new DocFile(fileName, fileName, "", filePath + fileName, false);
                    IndexHandler indexHandler = IndexHandler.getInstance();
                    indexHandler.addDoc(docFile);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect("upload.html");

    }

}
