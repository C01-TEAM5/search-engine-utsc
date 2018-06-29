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
    public static final String INDEX_KEY_OWNER = "Owner";
    public static final String INDEX_KEY_PATH = "Path";
    public static final String INDEX_KEY_STATUS = "Status";
    public static final String INDEX_KEY_TITLE = "Title";
    public static final String INDEX_KEY_TYPE = "Type";

    public static final int SEARCH_HITS_PER_PAGE = 10;

    public static final String[] VALIDDOCTYPES = {"pdf", "txt", "html", "docx"};
}
