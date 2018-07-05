package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.util.List;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fall2018.cscc01.team5.searchEngineWebApp.users.User;
import fall2018.cscc01.team5.searchEngineWebApp.util.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private User user;
	
	
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<String> userData = new ArrayList<String>();
        String data;
        
        // get data
        for (int i = 0; i < 4; i ++) {
            switch(i) {
            case 0: data = req.getParameter("query");
            case 1: data = req.getParameter("query");
            case 2: data = req.getParameter("query");
            case 3: data = req.getParameter("query");
            default: data = "";
                
            }
            
            if (data.length()==0) {
                return;
            } else {
                userData.add(data);
            }
        }
        
        // makes new user assuming list order
        // username, email, name, password
        try {
            user = new User(userData.get(0), userData.get(1),
                    userData.get(2), userData.get(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}
    


    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
        // registration if not null
        try {
		    if (user != null) {
	            AccountManager.register(user);   
	            Cookie cookie = new Cookie(Constants.CURRENT_USER, user.getName());
	            resp.addCookie(cookie);
		    }  
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

}
