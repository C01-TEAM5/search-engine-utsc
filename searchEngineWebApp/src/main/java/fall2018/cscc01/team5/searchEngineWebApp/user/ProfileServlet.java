package fall2018.cscc01.team5.searchEngineWebApp.user;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProfileServlet extends HttpServlet {

    public ProfileServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        RequestDispatcher view = req.getRequestDispatcher("profile.html");
        view.forward(req, resp);
    }
}