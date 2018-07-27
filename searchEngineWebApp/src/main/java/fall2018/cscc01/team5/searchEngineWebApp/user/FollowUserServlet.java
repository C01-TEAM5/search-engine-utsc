package fall2018.cscc01.team5.searchEngineWebApp.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

public class FollowUserServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        //String id = req.getParameter(Constants.SERVLET_PARAMETER_ID);
        String currentUser = null;
        try {
            currentUser = ServletUtil.getDecodedCookie(req.getCookies());
            if (!AccountManager.exists(currentUser)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Gson gson = new Gson();

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }

        Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {}.getType());
        String unfollow = map.get("unfollow");
        String id = map.get("id");

        try {
            User toAdd = AccountManager.getUser(id);

            if (unfollow.equalsIgnoreCase("true"))
                toAdd.removeFollower(currentUser);
            else
                toAdd.addFollower(currentUser);

            AccountManager.updateUser(toAdd.getUsername(), toAdd);
            PrintWriter output = resp.getWriter();
            output.print(new Gson().toJson(true));
            output.flush();
            return;
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
