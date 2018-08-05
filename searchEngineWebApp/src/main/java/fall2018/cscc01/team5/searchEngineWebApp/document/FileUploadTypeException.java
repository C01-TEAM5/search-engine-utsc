package fall2018.cscc01.team5.searchEngineWebApp.document;

public class FileUploadTypeException extends Exception {

    /**
     * Create an instance of this exception
     */
    public FileUploadTypeException () {
        super();
    }

    /**
     * Create an instance of this exception with a message
     */
    public FileUploadTypeException (String msg) {
        super(msg);
    }
}