package fall2018.cscc01.team5.searchEngineWebApp.servlets;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

        
	}
    


    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		

        Gson gson = new Gson();
        
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }   
        
        Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
        
        System.out.println(map.get("registerpsw"));
        
        // makes new user assuming list order
        // username, email, name, password
        try {
            user = new User(
                    map.get("registerusername"),
                    map.get("registeruemail"), 
                    map.get("registerusername"), 
                    map.get("regsiterpsw"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // registration if not null
        try {
		    if (user != null) {
	            boolean success = AccountManager.register(user);
	            if (success) {    	                
    	            Cookie cookie = new Cookie(Constants.CURRENT_USER, user.getName());
    	            resp.addCookie(cookie);
                }
		    }  
		} catch (Exception e) {
		    e.printStackTrace();
		}
        
        
        // successfully signed up
        PrintWriter output = resp.getWriter();
        output.print(gson.toJson("Success"));
        output.flush();
	}

}
