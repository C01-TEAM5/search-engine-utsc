package fall2018.cscc01.team5.searchEngineWebApp.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.gridfs.model.GridFSFile;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class FileManager {
    
    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static GridFSBucket grid = GridFSBuckets.create(database, "files");
    
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
     * @param fileStream the input stream containing the file contents
     * 
     * @return the id of the uploaded file
     */
    public static String upload(String fileName, String owner, boolean isPublic, String title, String fileType,
            int permission, String course, InputStream fileStream) {
        Document metadata = new Document();
        metadata.append(Constants.INDEX_KEY_FILENAME, fileName);
        metadata.append(Constants.INDEX_KEY_OWNER, owner);
        metadata.append(Constants.INDEX_KEY_STATUS, Boolean.toString(isPublic));
        metadata.append(Constants.INDEX_KEY_TITLE, title);
        metadata.append(Constants.INDEX_KEY_TYPE, fileType);
        metadata.append(Constants.INDEX_KEY_PERMISSION, Integer.toString(permission));
        metadata.append(Constants.INDEX_KEY_COURSE, course);
        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(1024)
                .metadata(metadata);
        
        return grid.uploadFromStream(fileName, fileStream, options).toHexString();
    }
    
    /**
     * Download a file from the database given its id and filename
     * 
     * @param id the id of the file
     * @param fileName the name of the file
     * 
     * @return the temporary path containing the file
     * @throws IOException
     */
    public static String download(ObjectId id, String fileName) throws IOException {
        
        File tmpPath = new File(Constants.FILE_PUBLIC_PATH);
        if (!tmpPath.exists()) tmpPath.mkdirs();
        
        String path = Constants.FILE_PUBLIC_PATH + id.toHexString() + "-" + fileName;
        
        FileOutputStream stream = new FileOutputStream(path);
        
        grid.downloadToStream(id, stream);
        stream.close();

        return path;
    }

    /**
     * Runs the given indexer with all the files in the database.
     *
     * @param ih the indexer to run to add files
     * @throws IOException
     */
    public static void indexFiles (final IndexHandler ih) throws IOException {
        
        grid.find().forEach(new Block<GridFSFile>() {
            @Override
            public void apply(final GridFSFile file) {
                Document metadata = file.getMetadata();
                ObjectId id = file.getId().asObjectId().getValue();
                String tmpPath = "";
                try {
                    tmpPath = download(id, file.getFilename());
                } catch (IOException e) {
                    return;
                }
                
                DocFile toAdd = new DocFile(metadata.getString(Constants.INDEX_KEY_FILENAME), metadata.getString(Constants.INDEX_KEY_TITLE),
                        metadata.getString(Constants.INDEX_KEY_OWNER), tmpPath, metadata.getString(Constants.INDEX_KEY_STATUS).equalsIgnoreCase("true"));
                toAdd.setCourseCode(metadata.getString(Constants.INDEX_KEY_COURSE));
                toAdd.setPermissions(Integer.parseInt(metadata.getString(Constants.INDEX_KEY_PERMISSION)));
                toAdd.setId(id.toHexString());
                
                ih.addDoc(toAdd);
            }
        });
        if (new File(Constants.FILE_PUBLIC_PATH).exists())
            FileUtils.cleanDirectory(new File(Constants.FILE_PUBLIC_PATH));
    }

}
