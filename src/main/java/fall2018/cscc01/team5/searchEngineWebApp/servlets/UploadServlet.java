package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;

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
        //throw new ServletException("GET method used with " + getClass().getName()+": POST method required.");
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
            return;
         }
        
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(maxMemSize); // maximum size that will be stored in memory
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxFileSize); // maximum file size to be uploaded.
        
        try {
            // parse multiple files   
            List items = upload.parseRequest(req);
            Iterator itemIterator  = items.iterator();
            
            while (itemIterator.hasNext()) {
                FileItem item = (FileItem) itemIterator.next();
                
                // gets file data
                if (!item.isFormField()) {
                    
                }
                
            }   
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
        /*
        try { 
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(req);
       
            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");  
            out.println("</head>");
            out.println("<body>");
      
            while ( i.hasNext () ) {
               FileItem fi = (FileItem)i.next();
               if ( !fi.isFormField () ) {
                  // Get the uploaded file parameters
                  String fieldName = fi.getFieldName();
                  String fileName = fi.getName();
                  String contentType = fi.getContentType();
                  boolean isInMemory = fi.isInMemory();
                  long sizeInBytes = fi.getSize();
               
                  // Write the file
                  if( fileName.lastIndexOf("\\") >= 0 ) {
                     file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
                  } else {
                     file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                  }
                  fi.write( file ) ;
                  out.println("Uploaded Filename: " + fileName + "<br>");
               }
            }
        out.println("</body>");
        out.println("</html>");
        } catch(Exception e) {
            e.printStackTrace();
        }
        */
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
    
    private void storeToIndex(String filename, String title, String owner, String path, boolean isPublic) {
        
        docFile  = new DocFile("filename", "title", "owner", "path", true);
        IndexHandler indexHandler = new IndexHandler("");
        indexHandler.addDoc(docFile);
    }

}
