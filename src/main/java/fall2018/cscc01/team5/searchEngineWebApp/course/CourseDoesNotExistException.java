package fall2018.cscc01.team5.searchEngineWebApp.course;

public class CourseDoesNotExistException extends Throwable {
    /**
     * Create an instance of this exception
     */
    public CourseDoesNotExistException() {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public CourseDoesNotExistException(String msg) {
        super(msg);
    }
}
