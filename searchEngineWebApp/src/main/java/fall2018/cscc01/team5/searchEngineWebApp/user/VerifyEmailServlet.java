package fall2018.cscc01.team5.searchEngineWebApp.user;

import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.user.Notifications.NotificationManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
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

public class VerifyEmailServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String id = null;

        try {
            id = Validator.simpleDecode(req.getParameter(Constants.SERVLET_PARAMETER_ID));
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            User user = AccountManager.getUser(id);
            if (user.isEmailVerified()) {
                req.setAttribute("msg", "Your email has already been verified.");
            }
            else {
                AccountManager.verifyUserEmail(user);
                req.setAttribute("msg", "Your email has been verified. You are now authorized to use Search Engine UTSC. Please login.");
            }

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

            RequestDispatcher view = req.getRequestDispatcher("templates/emailVerified.jsp");
            view.forward(req, resp);
        } catch (InvalidUsernameException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (EmailAlreadyExistsException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (ServletException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}
