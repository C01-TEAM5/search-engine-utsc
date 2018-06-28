package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;

public class UploadServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    private boolean isMultipart;
    private String filePath;
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file ;
    private DocFile docFile;
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        filePath = getServletContext().getInitParameter("file-upload");
    }

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("Show search result.");   // delete
        
        
        
        // TODO: get query and filter
        // TODO: display result on web page, performSearch()
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, java.io.IOException {
        
        // check upload request
        isMultipart = ServletFileUpload.isMultipartContent(req);
        resp.setContentType("text/html");
        java.io.PrintWriter out = resp.getWriter( );
        
        // empty return
        if( !isMultipart ) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<p>No file uploaded</p>"); 
            out.println("</body>");
            out.println("</html>");
            return;
         }
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
     
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
     
        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );
        
        
        
        
        
        
    }
    
    
    
    

    /* Perform searching, return search result (change void)
     * If we want more info than just file name in the search result,
     * then search result return in SearchResult type
     *
     * Assume the query and filter we get are all in string type for now
     */
    public String performSearch (String queryString, String fileType, String uploader) {
        return null;
        //TODO
    }

}
