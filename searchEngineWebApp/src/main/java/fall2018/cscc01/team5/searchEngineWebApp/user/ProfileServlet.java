package fall2018.cscc01.team5.searchEngineWebApp.user;

import com.google.gson.Gson;
import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ProfileServlet extends HttpServlet {

    public ProfileServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        boolean get = req.getParameterMap().containsKey(Constants.SERVLET_PARAMETER_GET);

        if (id == null || !AccountManager.exists(id.toLowerCase())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (get) {
            PrintWriter out = resp.getWriter();
            try {
                out.println(new Gson().toJson(AccountManager.getUser(id.toLowerCase())));
                out.flush();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidUsernameException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return;
        }
        RequestDispatcher view = req.getRequestDispatcher("templates/profile.jsp");
        view.forward(req, resp);
    }

    private String getCurrentUser(Cookie[] cookies) {
        String res = "";
        if (cookies == null) return res;
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("currentUser")) res = cookie.getValue();
        }

        return res;
    }
}
