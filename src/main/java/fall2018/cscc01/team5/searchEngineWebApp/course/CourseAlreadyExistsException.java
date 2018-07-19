package fall2018.cscc01.team5.searchEngineWebApp.course;

public class CourseAlreadyExistsException extends Exception {
    /**
     * Create an instance of this exception
     */
    public CourseAlreadyExistsException() {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public CourseAlreadyExistsException(String msg) {
        super(msg);
    }
}
