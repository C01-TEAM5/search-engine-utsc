package fall2018.cscc01.team5.searchEngineWebApp.user.register;

import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

@WebServlet("/register")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public SignUpServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        
        Gson gson = new Gson();
        
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }   
        
        Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {}.getType());

        // makes new user assuming list order
        // username, email, name, password
        User user = null;
        try {
           user = new User(
                    map.get("username"),
                    map.get("email"), 
                    map.get("name"), 
                    map.get("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // registration if not null
        try {
            if (user != null) {
                try {
                    AccountManager.register(user);
                    Cookie cookie = new Cookie(Constants.CURRENT_USER, user.getName());
                    resp.addCookie(cookie);

                    // successfully signed up
                    PrintWriter output = resp.getWriter();
                    output.print(gson.toJson("Success"));
                    output.flush();
                }
                catch (UsernameAlreadyExistsException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "username");
                }
                catch (EmailAlreadyExistsException e) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "email");
                }
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}