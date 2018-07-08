package fall2018.cscc01.team5.searchEngineWebApp.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

@WebServlet("/signout")
public class SignOutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public SignOutServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie cookie=new Cookie(Constants.CURRENT_USER,"");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        
        // successfully signed out
        Gson gson = new Gson();
        PrintWriter output = resp.getWriter();
        output.print(gson.toJson("Success"));
        output.flush();
    }

}
