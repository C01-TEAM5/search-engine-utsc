package fall2018.cscc01.team5.searchEngineWebApp.server;

import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.Notifications.NotificationManager;
import fall2018.cscc01.team5.searchEngineWebApp.util.ServletUtil;
import org.apache.commons.codec.DecoderException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("application/json");

        String currentUser = null;
        try {
            currentUser = ServletUtil.getDecodedCookie(req.getCookies());
        }
        catch (InvalidKeySpecException e) {}
        catch (NoSuchAlgorithmException e) {}
        catch (DecoderException e) {}
        catch (Exception e) {}

        if (currentUser != null && AccountManager.exists(currentUser)) {
            req.setAttribute("loggedIn", true);
            req.setAttribute("notifications", NotificationManager.getNotifications(currentUser));
            req.setAttribute("hasNew", NotificationManager.hasNew(currentUser));
        }
        else {
            req.setAttribute("loggedIn", false);
            req.setAttribute("hasNew", false);
        }

        RequestDispatcher view = req.getRequestDispatcher("index.jsp");
        view.forward(req, resp);
    }
}
