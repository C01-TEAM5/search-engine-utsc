package fall2018.cscc01.team5.searchEngineWebApp.user.register;

public class UsernameAlreadyExistsException extends Exception {

    /**
     * Create an instance of this exception
     */
    public UsernameAlreadyExistsException () {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public UsernameAlreadyExistsException (String msg) {
        super(msg);
    }
}
