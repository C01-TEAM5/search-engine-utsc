package fall2018.cscc01.team5.searchEngineWebApp.util;

import static org.junit.Assert.fail;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import fall2018.cscc01.team5.searchEngineWebApp.users.User;

public class AccountManagerTest {
	
	private static MongoClientURI uri = new MongoClientURI(
		    "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

	private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static MongoCollection<Document> usersCollection = database.getCollection("users");
    
    private User[] users = new User[5];
    private Document[] docs = new Document[5];
	
    

	@Before
	public void setUp() throws Exception {
		
		users[0] = new User("user0", "user0@gmail.com", "user0", "testpw");
		users[1] = new User("user1", "user1@gmail.com", "user1", "testpw");
		users[2] = new User("user2", "user2@gmail.com", "user2", "testpw");
		users[3] = new User("user3", "user3@gmail.com", "user3", "testpw");
		users[4] = new User("user4", "user4@gmail.com", "user4", "testpw");
		
		for (int i = 0; i < users.length; i++) {
			docs[i] = new Document("name", users[i].getName())
	    			.append("username", users[i].getUsername())
	    			.append("hash", users[i].getHash());
		}		
		
	}

	@After
	public void tearDown() throws Exception {
		for (int i = 0; i < users.length; i++) {
			usersCollection.deleteOne(Filters.eq("username", users[i].getUsername()));
		}
	}
	
	@Test
	public void testSuccessfulRegister() {
		for (int i = 0; i < users.length; i++) {
			Assert.assertTrue(AccountManager.register(users[i]));			
		}
	}
	
	@Test
	public void testFailedRegister() {
		for (int i = 0; i < users.length; i++) {
			Assert.assertTrue(AccountManager.register(users[i]));			
		}
		
		for (int i = 0; i < users.length; i++) {
			Assert.assertFalse(AccountManager.register(users[i]));			
		}
	}

	@Test
	public void testLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogout() {
		fail("Not yet implemented");
	}

}
