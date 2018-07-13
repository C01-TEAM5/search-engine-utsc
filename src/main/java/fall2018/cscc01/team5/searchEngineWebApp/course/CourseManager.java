package fall2018.cscc01.team5.searchEngineWebApp.course;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.UserValidator;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;
import org.apache.commons.codec.DecoderException;
import org.bson.Document;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CourseManager {

    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static MongoCollection<Document> coursesCollection = database.getCollection("courses");

    /*
     * TO:DO replace Object with User
     */

    /**
     * Add a given Course to the database.
     *
     * @param course - a Course to add to the database
     */
    public static void addCourse (Course course) throws CourseAlreadyExistsException {

        Document doc = coursesCollection.find(Filters.eq("code", course.getCode())).first();
        if (doc != null) throw new CourseAlreadyExistsException();

        doc = new Document("code", course.getCode())
                .append("name", course.getName())
                .append("description", course.getDescription())
                .append("size", course.getSize())
                .append("instructors", course.getAllInstructors())
                .append("files", course.getAllFiles())
                .append("students", course.getAllStudents())
                .append("teachingAssistants", course.getAllTAs());


        coursesCollection.insertOne(doc);
    }

    /**
     * Update a course in the database with new data.
     *
     * @param code the old code of the course
     * @param course the new course data
     */
    public static void updateCourse (String code, Course course) throws CourseDoesNotExistException {
        Document doc = coursesCollection.find(Filters.eq("code", code)).first();
        if (doc == null) throw new CourseDoesNotExistException();

        doc = new Document("code", course.getCode())
                .append("name", course.getName())
                .append("description", course.getDescription())
                .append("size", course.getSize())
                .append("instructors", course.getAllInstructors())
                .append("files", course.getAllFiles())
                .append("students", course.getAllStudents())
                .append("teachingAssistants", course.getAllTAs());

        coursesCollection.updateOne(Filters.eq("code", code), new Document("$set", doc));
    }

    /**
     * Return a course given a course code
     * @param code the code of the course
     */
    public static Course getCourse(String code) throws CourseDoesNotExistException {
        Document doc = coursesCollection.find(Filters.eq("code", code)).first();
        if (doc == null) throw new CourseDoesNotExistException();


    }
}
