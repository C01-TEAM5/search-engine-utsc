package fall2018.cscc01.team5.searchEngineWebApp.util;

import fall2018.cscc01.team5.searchEngineWebApp.user.Validator;
import org.apache.commons.codec.DecoderException;

import javax.servlet.http.Cookie;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ServletUtil {

    public static String getDecodedCookie(Cookie[] cookies) throws InvalidKeySpecException, NoSuchAlgorithmException, DecoderException {
        String res = "";
        if (cookies == null) return res;
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("currentUser")) res = cookie.getValue();
        }

        return Validator.simpleDecode(res);
    }
}
