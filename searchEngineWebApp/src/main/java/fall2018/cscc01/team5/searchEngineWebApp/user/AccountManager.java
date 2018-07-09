package fall2018.cscc01.team5.searchEngineWebApp.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import org.apache.commons.codec.DecoderException;
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
    public static void register (User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {

        if (usersCollection.find(Filters.eq("username", user.getUsername())).first() != null)
            throw new UsernameAlreadyExistsException();
        if (usersCollection.find(Filters.eq("email", user.getEmail())).first() != null)
            throw new EmailAlreadyExistsException();

        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("username", user.getUsername())
                .append("hash", user.getHash())
                .append("permission", user.getPermission());

        usersCollection.insertOne(doc);
    }

    /**
     * Given a User check authenticate the users with the database
     *
     * @param username - the username of the user
     * @param pass - the password of the user
     * @return true if users matches database record
     * @throws DecoderException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static boolean login (String username, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException, InvalidUsernameException {

        Document doc = usersCollection.find(Filters.eq("username", username)).first();
        if (doc == null) throw new InvalidUsernameException();
        if (!UserValidator.validatePassword(pass, doc.getString("hash"))) return false;

        return true;
    }

    public static int getPermission(String username) {
        return (int) usersCollection.find(Filters.eq("username", username)).first().get("permission");
    }

    /**
     * Update an exsisting users's data with new data
     * @param username
     * @param user
     */
    public static void updateUser (String username, User user) throws InvalidUsernameException, EmailAlreadyExistsException, UsernameAlreadyExistsException {

        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("username", user.getUsername())
                .append("hash", user.getHash())
                .append("permission", user.getPermission());

        usersCollection.updateOne(Filters.eq("username", username), new Document("$set", doc));
    }


    /**
     * Given a User, authenticate and create a login entry
     *
     * @param user - an instance of User with information to logout
     * @return true if logout was successful
     */
    public static boolean logout (User user) {

        // we probably will not use this,
        // logout will only be used if we want to record anything in the database when a users logs out
        // example: last active login

        return true;
    }

}
