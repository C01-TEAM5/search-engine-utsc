package fall2018.cscc01.team5.searchEngineWebApp.user.login;

public class InvalidUsernameException extends Exception {

    /**
     * Create an instance of this exception
     */
    public InvalidUsernameException () {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public InvalidUsernameException (String msg) {
        super(msg);
    }
}
