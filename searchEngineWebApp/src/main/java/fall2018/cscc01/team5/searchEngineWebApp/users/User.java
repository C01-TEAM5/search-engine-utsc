package fall2018.cscc01.team5.searchEngineWebApp.users;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import com.mongodb.BasicDBList;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import fall2018.cscc01.team5.searchEngineWebApp.util.UserValidator;

public class User {

    private String email;
    private String name;
    private String username;
    private String hash;
    private int permission;

    public User (String username, String email, String name, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.username = username;
        this.email = email;
        this.name = name;
        this.hash = UserValidator.getSaltedHash(password, UserValidator.getSalt());
        this.permission = Constants.PERMISSION_ALL;
    }

    /**
     * Get users name
     *
     * @return username
     */
    public String getUsername () {
        return username;
    }


    /**
     * Get email
     *
     * @return email
     */
    public String getEmail () {
        return this.email;
    }


    /**
     * get Name
     *
     * @return name
     */
    public String getName () {
        return this.name;
    }


    /**
     * get secure password/hash
     *
     * @return hash
     */
    public String getHash () {
        return this.hash;
    }

    /**
     * Return the permission attached to this User.
     * @return a permission attached to this User
     */
    public int getPermission() {
        return this.permission;
    }

    /**
     * Replace this Users permission.
     * @param perm - the replacement permission
     */
    public void setPermissions(int perm) {
        this.permission = perm;
    }

    /**
     * Return a string representation of this user.
     *
     * @return a string representation of this user
     */
    @Override
    public String toString () {
        String result = "User: [Username: " + username
                + " email: " + email
                + " name: " + name
                + " hashPassword: " + hash + "]";
        return result;
    }
}
