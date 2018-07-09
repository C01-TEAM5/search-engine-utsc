package fall2018.cscc01.team5.searchEngineWebApp.user.register;

public class EmailAlreadyExistsException extends Exception {

    /**
     * Create an instance of this exception
     */
    public EmailAlreadyExistsException() {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
