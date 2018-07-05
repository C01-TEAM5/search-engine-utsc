package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fall2018.cscc01.team5.searchEngineWebApp.users.User;

public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private User user;
	
	
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.getWriter().append("Served at: ").append(req.getContextPath());
	}


    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
		    if (user != null) {
	            register(user);   
		    }  
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

}
