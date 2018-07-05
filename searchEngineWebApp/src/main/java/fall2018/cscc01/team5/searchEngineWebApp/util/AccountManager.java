package fall2018.cscc01.team5.searchEngineWebApp.util;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import fall2018.cscc01.team5.searchEngineWebApp.users.User;

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
     * @return true if registration is successful, false otherwise
     */
    public static boolean register (User user) {
    	
    	if (usersCollection.find(Filters.eq("username", user.getUsername())).first() != null) return false;
    	
    	Document doc = new Document("name", user.getName())
    			.append("email", user.getEmail())
    			.append("username", user.getUsername())
    			.append("hash", user.getHash());
    	
    	usersCollection.insertOne(doc);
    	
    	return true;
    }
    
    /**
     * Given a User check authenticate the users with the database
     * 
     * @param user - an instance of User with information to validate
     * @return true if users matches database record
     */
    public static boolean login (User user) {
    	
    	
    	
    	return false;
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
    	
    	return false;
    }

}
