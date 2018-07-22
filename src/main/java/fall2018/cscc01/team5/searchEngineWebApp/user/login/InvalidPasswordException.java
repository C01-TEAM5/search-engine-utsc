package fall2018.cscc01.team5.searchEngineWebApp.user.login;

public class InvalidPasswordException extends Exception {

    /**
     * Create an instance of this exception
     */
    public InvalidPasswordException () {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public InvalidPasswordException (String msg) {
        super(msg);
    }
}
