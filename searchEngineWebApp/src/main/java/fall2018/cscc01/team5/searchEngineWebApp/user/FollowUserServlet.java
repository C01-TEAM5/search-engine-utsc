package fall2018.cscc01.team5.searchEngineWebApp.user;

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

public class FollowUserServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        String currentUser = null;
        try {
            currentUser = ServletUtil.getDecodedCookie(req.getCookies());
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            User toAdd = AccountManager.getUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
