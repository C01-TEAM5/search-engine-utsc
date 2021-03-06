package fall2018.cscc01.team5.searchEngineWebApp.course;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.List;

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
    public static UpdateResult updateCourse (String code, Course course) throws CourseDoesNotExistException {
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

        return coursesCollection.updateOne(Filters.eq("code", code), new Document("$set", doc));
    }

    /**
     * Return a course given a course code
     * @param code the code of the course
     */
    public static Course getCourse(String code) throws CourseDoesNotExistException {
        
        Document doc = coursesCollection.find(Filters.eq("code", code)).first();
        if (doc == null) throw new CourseDoesNotExistException();

        Course result = new Course(doc.getString("name"), code, doc.getInteger("size"), new String[] {});
        result.setDescription(doc.getString("description"));
        result.setInstructors((List<String>) doc.get("instructors"));
        result.setFiles((List<String>) doc.get("files"));
        result.setStudents((List<String>) doc.get("students"));
        result.setTAs((List<String>) doc.get("teachingAssistants"));
        
        return result;
    }

    public static boolean courseExists(String code) {
        code = code.toLowerCase();
        return coursesCollection.find(Filters.eq("code", code)).first() != null;
    }

}
