package fall2018.cscc01.team5.searchEngineWebApp.document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import fall2018.cscc01.team5.searchEngineWebApp.course.Course;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseDoesNotExistException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

/**
 * Uploader is responsible for uploading a file to the database
 * and making sure it is indexed correctly on upload.
 *
 */
public class Uploader {

    /**
     * Method that is called to upload a file to the database and index it in the search.
     * 
     * @param fileInfo The DocFile of the file we are uploading
     * @param fileStream The InputStream file that is being uploaded
     * @throws CourseDoesNotExistException 
     * @throws IOException 
     * @throws FileUploadTypeException 
     */
    public static String handleUpload(DocFile fileInfo, InputStream fileStream) throws CourseDoesNotExistException, IOException, FileUploadTypeException {
        
        String fileType = fileInfo.getFileType();
        
        if (!(fileType.equals("docx") || fileType.equals("pdf") || 
                fileType.equals("txt") || fileType.equals("html"))) {
            throw new FileUploadTypeException(); 
        }
        
        String filePath = getUploadPath(fileInfo.getOwner());
        
        // creates the save directory if it does not exists
        File fileSaveDir = new File(filePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        
        //If the file already exists, do not upload. Exit the method.
        File targetFile = new File(filePath + fileInfo.getFilename());
        if (targetFile.isFile()) {
            return null;
        }
        
        //File does not exist yet.
        //Upload File to the database
        String fileId = FileManager.upload(fileInfo.getFilename(), fileInfo.getOwner(), true, fileInfo.getFilename(), 
                fileInfo.getFileType(), fileInfo.getPermission(), fileInfo.getCourseCode(), 
                fileInfo.getId(), fileStream);
        fileInfo.setId(fileId);
        
        //Update Course if necessary
        String courseCode = fileInfo.getCourseCode();
        if (courseCode != "" && CourseManager.courseExists(courseCode.toLowerCase())) {
            Course c = CourseManager.getCourse(courseCode);
            c.addFile(fileInfo.getId());
            CourseManager.updateCourse(courseCode, c);
        }
        
        //Add DocFile to the index
        fileInfo.setPath(FileManager.download(fileInfo.getId(), fileInfo.getFileType()));
        IndexHandler indexHandler = IndexHandler.getInstance();
        indexHandler.addDoc(fileInfo);
        
        return fileId;
    }
    
    /**
     * Returns the upload path where the current users files will go.
     * 
     * @param currentUser
     */
    public static String getUploadPath(String currentUser) {
        
        return Constants.FILE_PUBLIC_PATH + currentUser + File.separator;
        
    }
    
}
