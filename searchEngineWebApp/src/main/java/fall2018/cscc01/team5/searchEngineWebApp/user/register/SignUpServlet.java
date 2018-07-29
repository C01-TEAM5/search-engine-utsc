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

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.Validator;
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
        String username = Jsoup.clean(map.get("username"), Whitelist.basic());
        String email = Jsoup.clean(map.get("email"), Whitelist.basic());
        String name = Jsoup.clean(map.get("name"), Whitelist.basic());
        String password = Jsoup.clean(map.get("password"), Whitelist.basic());
        String permission = Jsoup.clean(map.get("permission"), Whitelist.basic());

        try {
           user = new User(
                    username,
                    email, 
                    name, 
                    password);
           user.setPermissions(Integer.parseInt(permission));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad user values");
            e.printStackTrace();
            return;
        }
        
        
        // registration if not null
        try {
            if (user != null) {
                try {
                    AccountManager.register(user);
//                    Cookie cookie = new Cookie(Constants.CURRENT_USER, Validator.simpleEncrypt(user.getUsername()));
//                    Cookie cookieName = new Cookie(Constants.CURRENT_USER_NAME, user.getName());
//                    resp.addCookie(cookie);
//                    resp.addCookie(cookieName);
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