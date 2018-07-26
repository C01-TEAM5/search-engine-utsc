package fall2018.cscc01.team5.searchEngineWebApp.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class AccountManager {

    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static MongoCollection<Document> usersCollection = database.getCollection("users");

    /*
     * TO:DO replace Object with User
     */

    /**
     * Register a users in the database.
     *
     * @param user - an instance of User with information to create a database record
     */
    public static void register(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException, EmailException, NoSuchAlgorithmException, InvalidKeySpecException {

        if (usersCollection.find(Filters.eq("username", user.getUsername())).first() != null)
            throw new UsernameAlreadyExistsException();
        if (usersCollection.find(Filters.eq("email", user.getEmail())).first() != null)
            throw new EmailAlreadyExistsException();

        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("username", user.getUsername())
                .append("hash", user.getHash())
                .append("courses", user.getCourses())
                .append("desc", user.getDescription())
                .append("permission", user.getPermission());

        usersCollection.insertOne(doc);
        sendVerificationEmail(user);
    }

    public static void sendVerificationEmail(User user) throws EmailException, InvalidKeySpecException, NoSuchAlgorithmException {

        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(Constants.EMAIL_USER, Constants.EMAIL_PASS));
        email.setSSL(true);
        email.setFrom(Constants.EMAIL_USER);
        email.setSubject("Verify your email - Search Engine UTSC");
        email.setMsg(constructVerifyMsg(user));
        email.addTo(user.getEmail());
        email.send();

    }

    private static String constructVerifyMsg (User u) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String verifyId = Validator.getSaltedHash(u.getEmail(), Validator.getSalt());

        StringBuilder msg = new StringBuilder();
        msg.append("Hello " + u.getName() + ",");
        msg.append(" ");
        msg.append("You need to verify your email to use the Search Engine UTSC.");
        msg.append("Please click the following link to verify your email.");
        msg.append(Constants.EMAIL_VERIFY_PREFIX + verifyId);
        msg.append(" ");
        msg.append("Sincerely,");
        msg.append("Search Engine UTSC Team");

        return msg.toString();
    }

    /**
     * Set the email verified status of this user to true
     *
     * @param u the user to verify email
     * @throws InvalidUsernameException
     * @throws EmailAlreadyExistsException
     * @throws UsernameAlreadyExistsException
     */
    public static void verifyUserEmail(User u) throws InvalidUsernameException, EmailAlreadyExistsException, UsernameAlreadyExistsException {
        u.setEmailVerified(true);
        updateUser(u.getUsername(), u);
    }

    /**
     * Given a User check authenticate the users with the database
     *
     * @param username - the username of the user
     * @param pass     - the password of the user
     * @return true if users matches database record
     * @throws DecoderException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static boolean login(String username, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException, InvalidUsernameException {

        Document doc = usersCollection.find(Filters.eq("username", username)).first();
        if (doc == null) throw new InvalidUsernameException();
        if (!Validator.validateHash(pass, doc.getString("hash"))) return false;

        return true;
    }

    public static int getPermission(String username) {
        return (int) usersCollection.find(Filters.eq("username", username)).first().get("permission");
    }

    /**
     * Update an exsisting users's data with new data
     *
     * @param username
     * @param user
     */
    public static void updateUser(String username, User user) throws InvalidUsernameException, EmailAlreadyExistsException, UsernameAlreadyExistsException {

        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("username", user.getUsername())
                .append("hash", user.getHash())
                .append("courses", user.getCourses())
                .append("desc", user.getDescription())
                .append("permission", user.getPermission());

        usersCollection.updateOne(Filters.eq("username", username), new Document("$set", doc));
    }

    /**
     * Get a User from the database given a username
     *
     * @param username a username to search the database
     * @return
     * @throws InvalidUsernameException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static User getUser(String username) throws InvalidUsernameException, InvalidKeySpecException, NoSuchAlgorithmException {
        Document doc = usersCollection.find(Filters.eq("username", username)).first();
        if (doc == null) throw new InvalidUsernameException();

        User user = new User(doc.getString("username"), doc.getString("email"), doc.getString("name"), "");
        user.setPermissions((Integer) doc.get("permission"));
        user.setHash(doc.getString("hash"));
        user.setCourses((List<String>) doc.get("courses"));
        user.setDescription(doc.getString("desc"));

        return user;
    }
    
    /**
     * Check if a user exists
     * @param username the username to check
     * @return true if username exists in the database otherwise, false
     */
    public static boolean exists(String username) {
        return usersCollection.find(Filters.eq("username", username)).first() != null;
    }

    /**
     * Given a User, authenticate and create a login entry
     *
     * @param user - an instance of User with information to logout
     * @return true if logout was successful
     */
    public static boolean logout(User user) {

        // we probably will not use this,
        // logout will only be used if we want to record anything in the database when a users logs out
        // example: last active login

        return true;
    }

}
