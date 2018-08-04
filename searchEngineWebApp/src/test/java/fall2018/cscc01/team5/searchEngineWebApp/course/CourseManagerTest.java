package fall2018.cscc01.team5.searchEngineWebApp.course;

import static org.junit.Assert.*;
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


public class CourseManagerTest {

    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static MongoCollection<Document> usersCollection = database.getCollection("courses");

    private Course[] courses = new Course[5];

    @Before
    public void setUp() throws Exception {
        courses[0] = new Course("course 1", "csc1", 123, new String[] {"user1"});
        courses[1] = new Course("course 1", "csc2", 123, new String[] {"user1"});
        courses[2] = new Course("course 1", "csc3", 123, new String[] {"user1"});
        courses[3] = new Course("course 1", "csc4", 123, new String[] {"user1"});
        courses[4] = new Course("course 1", "csc5", 123, new String[] {"user1"});
        
        for (int i = 0; i < courses.length; i++) {
            usersCollection.deleteOne(Filters.eq("code", courses[i].getCode()));
        }
    }

    @After
    public void tearDown() throws Exception {
        for (int i = 0; i < courses.length; i++) {
            usersCollection.deleteOne(Filters.eq("code", courses[i].getCode()));
        }
    }

    @Test
    public void testSuccessfulAddCourse() throws CourseAlreadyExistsException {
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
            Assert.assertTrue(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first() != null);
        }
    }
    
    @Test(expected = CourseAlreadyExistsException.class)
    public void testFailedAddCourse() throws CourseAlreadyExistsException {
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
            Assert.assertTrue(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first() != null);
        }
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
        }
    }
    

    @Test
    public void testSuccessfulUpdateCourse() throws CourseAlreadyExistsException, CourseDoesNotExistException {
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
            Assert.assertTrue(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first() != null);
        }
        for (int i = 0; i < courses.length; i++) {
            courses[i].setName("changed!");
            CourseManager.updateCourse(courses[i].getCode(), courses[i]);
            Assert.assertEquals(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first().getString("name"),
                    "changed!");
        }
    }
    
    @Test(expected = CourseDoesNotExistException.class)
    public void testFailedUpdateCourse() throws CourseAlreadyExistsException, CourseDoesNotExistException {
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
            Assert.assertTrue(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first() != null);
        }
        for (int i = 0; i < courses.length; i++) {
            courses[i].setName("changed!");
            CourseManager.updateCourse("invalidCourse", courses[i]);
        }
    }
    

    @Test
    public void testSuccessfulGetCourse() throws CourseAlreadyExistsException,CourseDoesNotExistException {
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
            Assert.assertTrue(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first() != null);
        }
        
        for (int i = 0; i < courses.length; i++) {
            Course c = CourseManager.getCourse(courses[i].getCode());
            Assert.assertEquals(courses[i].getCode(),c.getCode());
        }
    }
    
    @Test(expected = CourseDoesNotExistException.class)
    public void testFailedGetCourse() throws CourseAlreadyExistsException,CourseDoesNotExistException {
        for (int i = 0; i < courses.length; i++) {
            CourseManager.addCourse(courses[i]);
            Assert.assertTrue(
                    usersCollection.find(Filters.eq("code", courses[i].getCode())).first() != null);
        }
        for (int i = 0; i < courses.length; i++) {
            Course c = CourseManager.getCourse("invalidCourse");
            Assert.assertEquals(courses[i].getCode(),c.getCode());
        }
    }

}
