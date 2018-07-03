package fall2018.cscc01.team5.searchEngineWebApp.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class AccountManager {
	
	private static MongoClientURI uri = new MongoClientURI(
		    "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

	private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("users");
    
	/*
	 * TO:DO replace Object with User 
	 */
    
    /**
     * Register a user in the database.
     * 
     * @param user - an instance of User with information to create a database record
     * @return true if registration is successful, false otherwise
     */
    public static boolean register (Object user) {
    	
    	
    	return false;
    }
    
    /**
     * Given a User check authenticate the user with the database
     * 
     * @param user - an instance of User with information to validate
     * @return true if user matches database record
     */
    public static boolean login (Object user) {
    	
    	//
    	
    	return false;
    }
    
    /**
     * Given a User, authenticate and create a login entry
     * 
     * @param user - an instance of User with information to logout
     * @return true if logout was successful
     */
    public static boolean logout (Object user) {
    	
    	// we probably will not use this, 
    	// logout will only be used if we want to record anything in the database when a user logs out
    	// example: last active login 
    	
    	return false;
    }

}
