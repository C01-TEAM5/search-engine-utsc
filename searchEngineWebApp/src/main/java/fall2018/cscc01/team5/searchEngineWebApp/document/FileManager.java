package fall2018.cscc01.team5.searchEngineWebApp.document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.amazonaws.ClientConfiguration;
import org.apache.commons.io.FileUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class FileManager {
    
    //private static AWSCredentials credentials = new BasicAWSCredentials(Constants.AWS_ACCESS_KEY,Constants.AWS_SECRET_KEY);
    private static AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .build();
    private static String bucketName = "search-engine-utsc";
    
    /**
     * Upload a given file along with its meta data to the database
     * 
     * @param fileName the name of the file
     * @param owner the owner of the file
     * @param isPublic true if this is a public file, false otherwise
     * @param title the title of this file
     * @param fileType the type of this file
     * @param permission the permission level of this file
     * @param course the course this file belongs to
     * @param id the DocFile id
     * @param fileStream the input stream containing the file contents
     * 
     * @return the id of the uploaded file
     */
    public static String upload(String fileName, String owner, boolean isPublic, String title, String fileType,
            int permission, String course, String id, InputStream fileStream) {
        
        ObjectMetadata metad = new ObjectMetadata();
        metad.addUserMetadata(Constants.INDEX_KEY_FILENAME, fileName);
        metad.addUserMetadata(Constants.INDEX_KEY_OWNER, owner);
        metad.addUserMetadata(Constants.INDEX_KEY_STATUS, Boolean.toString(isPublic));
        metad.addUserMetadata(Constants.INDEX_KEY_TITLE, title);
        metad.addUserMetadata(Constants.INDEX_KEY_TYPE, fileType);
        metad.addUserMetadata(Constants.INDEX_KEY_PERMISSION, Integer.toString(permission));
        metad.addUserMetadata(Constants.INDEX_KEY_COURSE, course);
        switch (fileType) {
        case "pdf":
            metad.setContentType("application/pdf");
            break;
        case "html":
            metad.setContentType("text/html");
            break;
        case "docx":
            metad.setContentType("text/msword");
            break;
        default:
            metad.setContentType("text/plain");
            break;
        }
        metad.setContentDisposition("inline");
        
        PutObjectRequest object = new PutObjectRequest(bucketName, id + "." + fileType, fileStream, metad);
               
        s3client.putObject(object);
        
        return id;
    }
    
    /**
     * Build a AWS public link given the id and the filetype
     * 
     * @param id the id of the file
     * @param fileType the type of the file
     * @return a String representation of the a aws public url 
     */
    public static String buildPublicUrl(String id, String fileType) {
        return Constants.AWS_PUBLIC_URL_PREFIX + id + "." + fileType;
    }
    
    /**
     * Download a file from the database given its id and filename
     * @param id the id of the file
     * @param fileType the type of the file
     * 
     * @return the temporary path containing the file
     * @throws IOException
     */
    public static String download(String id, String fileType) throws IOException {
        
        File tmpPath = new File(Constants.FILE_PUBLIC_BASE_PATH + Constants.FILE_PUBLIC_PATH);
        if (!tmpPath.exists()) tmpPath.mkdirs();
        
        String path = Constants.FILE_PUBLIC_BASE_PATH + Constants.FILE_PUBLIC_PATH + id + "." + fileType;
        
        S3Object object = s3client.getObject(bucketName, id + "." + fileType);
        FileUtils.copyInputStreamToFile(object.getObjectContent(), new File(path));
        object.close();

        return path;
    }
    
    /**
     * Runs the given indexer with all the files in the database.
     *
     * @param ih the indexer to run to add files
     * @throws IOException
     */
    public static void indexFiles (final IndexHandler ih) throws IOException {
        
        ObjectListing objectList = s3client.listObjects(bucketName);
        for (S3ObjectSummary summary: objectList.getObjectSummaries()) {
            S3Object object = s3client.getObject(bucketName, summary.getKey());
            ObjectMetadata metadata = object.getObjectMetadata(); 
            
            String filename = metadata.getUserMetaDataOf(Constants.INDEX_KEY_FILENAME);
            String owner = metadata.getUserMetaDataOf(Constants.INDEX_KEY_OWNER);
            boolean status = metadata.getUserMetaDataOf(Constants.INDEX_KEY_STATUS).equalsIgnoreCase(new Boolean(true).toString());
            String title = metadata.getUserMetaDataOf(Constants.INDEX_KEY_TITLE);
            String type = metadata.getUserMetaDataOf(Constants.INDEX_KEY_TYPE);
            int perm = Integer.parseInt(metadata.getUserMetaDataOf(Constants.INDEX_KEY_PERMISSION));
            String course = metadata.getUserMetaDataOf(Constants.INDEX_KEY_COURSE);
            
            DocFile file = new DocFile(filename, title, owner, "", status);
            file.setCourseCode(course);
            file.setPermissions(perm);
            file.setId(summary.getKey().split("\\.")[0]);
            file.setPath(download(summary.getKey().split("\\.")[0], type));
            ih.addDoc(file);
            object.close();
        }
        cleanTemporaryDownloads();
    }

    /**
     * Clean the temporary downloads folder
     * @throws IOException
     */
    public static void cleanTemporaryDownloads() throws IOException {
        if (new File(Constants.FILE_PUBLIC_BASE_PATH + Constants.FILE_PUBLIC_PATH).exists())
            FileUtils.cleanDirectory(new File(Constants.FILE_PUBLIC_BASE_PATH + Constants.FILE_PUBLIC_PATH));
    }

    /**
     * Update a file with new data
     *
     * @param id the id of the current file
     * @param file the updated file
     *
     * @return the new id of the file
     * @throws IOException
     */
    public static String update(String id, DocFile file) throws IOException {
        
        S3Object object = s3client.getObject(bucketName, id + "." + file.getFileType());
        ObjectMetadata metadata = object.getObjectMetadata();
        metadata.addUserMetadata(Constants.INDEX_KEY_FILENAME, file.getFilename());
        metadata.addUserMetadata(Constants.INDEX_KEY_OWNER, file.getOwner());
        metadata.addUserMetadata(Constants.INDEX_KEY_STATUS, Boolean.toString(file.isPublic()));
        metadata.addUserMetadata(Constants.INDEX_KEY_TITLE, file.getTitle());
        metadata.addUserMetadata(Constants.INDEX_KEY_TYPE, file.getFileType());
        metadata.addUserMetadata(Constants.INDEX_KEY_PERMISSION, Integer.toString(file.getPermission()));
        metadata.addUserMetadata(Constants.INDEX_KEY_COURSE, file.getCourseCode());
        
        s3client.putObject(new PutObjectRequest(bucketName, id + "." + file.getFileType(), object.getObjectContent(), metadata));

        object.close();
        
        return id;
    }

    /**
     * Delete given file from the database
     *
     * @param fileId the file to remove
     */
    public static void deleteFile(String fileId, String fileType) {
        s3client.deleteObject(bucketName, fileId + "." + fileType);
    }

    /**
     * Given a id check if a file exists
     *
     * @param fileId the file to check
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String fileId, String fileType) {
        try {
            S3Object object = s3client.getObject(bucketName, fileId + "." + fileType);
            boolean res = object != null;
            object.close();
            return res;
        }
        catch (Exception e) {
            return false;
        }
    }

}
