package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class UploadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html;charset=UTF-8");
	    
	    // request file input
	    final String path = request.getParameter("filePath");
	    final String fileName = request.getParameter("fileName");
	    final Part filePart = request.getPart("file");  //return collection of all part objects 
	    final InputStream filecontent = filePart.getInputStream();  //get the content of this part as InputStream

	    
	}

}
