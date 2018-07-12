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
     * Add a given course to the database.
     *
     * @param course - a course to add to the database
     */
    public static void addCourse (Object course) throws CourseAlreadyExistsException {

        Document doc = coursesCollection.find(Filters.eq("courseId", course.getId())).first();
        if (doc == null) throw new CourseAlreadyExistsException();

        doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("username", user.getUsername())
                .append("hash", user.getHash())
                .append("courses", user.getCourses())
                .append("permission", user.getPermission());

        coursesCollection.insertOne(doc);
    }
}
