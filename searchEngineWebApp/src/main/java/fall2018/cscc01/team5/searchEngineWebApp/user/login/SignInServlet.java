package fall2018.cscc01.team5.searchEngineWebApp.user.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

@WebServlet("/signin")
public class SignInServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public SignInServlet() {
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

        // registration if not null
        try {
            boolean success = AccountManager.login(map.get("username"), map.get("password"));
            if (success) {
                Cookie cookie = new Cookie(Constants.CURRENT_USER, map.get("username"));
                resp.addCookie(cookie);
                
                // successfully signed in
                PrintWriter output = resp.getWriter();
                output.print(gson.toJson("Success"));
                output.flush();
                
            }
            else resp.sendError(HttpServletResponse.SC_FORBIDDEN, "password");

        } catch (InvalidUsernameException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "username");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

