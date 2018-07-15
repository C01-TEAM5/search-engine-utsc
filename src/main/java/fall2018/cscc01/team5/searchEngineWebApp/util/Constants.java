package fall2018.cscc01.team5.searchEngineWebApp.util;

import java.io.File;

public class Constants {

    public static final String FILETYPE_DOCX = "docx";
    public static final String FILETYPE_HTML = "html";
    public static final String FILETYPE_PDF = "pdf";
    public static final String FILETYPE_TXT = "txt";
    
    public static final String FILE_UPLOAD_PATH = "file-uploads" + File.separator;

    public static final String INDEX_DIRECTORY = "index" + File.separator + "index.lucene";

    public static final String INDEX_KEY_CONTENT = "Content";
    public static final String INDEX_KEY_FILENAME = "Filename";
    public static final String INDEX_KEY_ID = "ID";
    public static final String INDEX_KEY_OWNER = "Owner";
    public static final String INDEX_KEY_PATH = "Path";
    public static final String INDEX_KEY_PERMISSION = "Permission";
    public static final String INDEX_KEY_STATUS = "Status";
    public static final String INDEX_KEY_TITLE = "Title";
    public static final String INDEX_KEY_TYPE = "Type";
    public static final String INDEX_KEY_COURSE = "Course";    
    
    public static final int SEARCH_HITS_PER_PAGE = 10;
    
    public static final String SERVLET_PARAMETER_COURSE_ID = "id";
    public static final String SERVLET_PARAMETER_COURSE_REMOVE_STUDENT = "removeStudent";
    public static final String SERVLET_PARAMETER_COURSE_ADD_STUDENT = "addStudent";
    public static final String SERVLET_PARAMETER_COURSE_ADD_INSTRUCTOR = "addInstructor";
    public static final String SERVLET_PARAMETER_COURSE_REMOVE_INSTRUCTOR = "removeInstructor";
    public static final String SERVLET_PARAMETER_COURSE_ADD_FILE = "addFile";
    public static final String SERVLET_PARAMETER_COURSE_REMOVE_FILE = "removeFile";

    public static final String[] VALIDDOCTYPES = {"pdf", "txt", "html", "docx"};
    
    public static final String CURRENT_USER = "currentUser";

    public static final int PERMISSION_ALL = 0;
    public static final int PERMISSION_INSTRUCTOR = 3;
    public static final int PERMISSION_STUDENT = 2;
    public static final int PERMISSION_TA = 1;

}
