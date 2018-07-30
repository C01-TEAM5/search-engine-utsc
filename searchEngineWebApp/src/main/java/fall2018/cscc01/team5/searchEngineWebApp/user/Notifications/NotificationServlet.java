package fall2018.cscc01.team5.searchEngineWebApp.user.Notifications;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.Validator;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

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

public class NotificationServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Gson gson = new Gson();

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = req.getReader().readLine()) != null) {
            sb.append(s);
        }

        Map<String, String> map = gson.fromJson(sb.toString(), new TypeToken<HashMap<String, String>>() {}.getType());

        String id = map.get("id");
        String clear = map.get("clear");
        String delete = map.get("delete");

        PrintWriter output = resp.getWriter();
        if (clear != null && clear.equalsIgnoreCase("true")) {
            NotificationManager.openNotification(id);
            output.print(new Gson().toJson("success"));
        }
        else if (delete != null && delete.equalsIgnoreCase("true")) {
            NotificationManager.removeNotification(id);
            output.print(new Gson().toJson("success"));
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        output.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
}
