package fall2018.cscc01.team5.searchEngineWebApp.document;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.mail.EmailException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import fall2018.cscc01.team5.searchEngineWebApp.course.Course;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.course.CourseDoesNotExistException;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.EmailAlreadyExistsException;
import fall2018.cscc01.team5.searchEngineWebApp.user.register.UsernameAlreadyExistsException;

public class UploaderTest {
    
    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static MongoCollection<Document> usersCollection = database.getCollection("users");
    User testUser;
    DocFile txtFile = new DocFile("text1.txt","Dog Story","user0","text1.txt",true);
    DocFile docxFile = new DocFile("fakecourse.docx","Fake Course","user0","fakecourse.docx",true);
    String uploadIDtxt;
    
    @Before 
    public void setup() throws NoSuchAlgorithmException, InvalidKeySpecException, UsernameAlreadyExistsException, EmailAlreadyExistsException, EmailException, IOException{
        //Use user0 for testing
        testUser = new User("uuser0", "uuser0@gmail.com", "uuser0", "testpw");
        usersCollection.deleteOne(Filters.eq("username", testUser.getUsername()));
        AccountManager.register(testUser);  
        
        generateTxt();
        
    }
    
    @After
    public void cleanup() throws IOException, ParseException {
        usersCollection.deleteOne(Filters.eq("username", testUser.getUsername()));
        removeFiles();
        IndexHandler handler = IndexHandler.getInstance();
        handler.removeDoc(txtFile);
        FileManager.deleteFile(uploadIDtxt, "txt");
    }
    
    @Test
    public void testUploadPath() {
        
        String currentUser = "andrew";
        String expectedPath = "tmp" + File.separator + "andrew" + File.separator;
        assertEquals(expectedPath,Uploader.getUploadPath(currentUser));

    }
    
    /* Test Uploader Handler with uploading a file to the database
     * 
     */
    @Test
    public void testUpload() throws IOException, CourseDoesNotExistException, FileUploadTypeException {
        
        File txt = new File("text1.txt");
        InputStream stream = new FileInputStream(txt);
        String uploadID = Uploader.handleUpload(txtFile, stream);
        stream.close();
        
        assertEquals(true, FileManager.fileExists(uploadID, "txt"));
    }
    
    /* Test Uploading a file type that is not within the specified filetypes
     * 
     */
    @Test(expected = FileUploadTypeException.class)
    public void testIncorrectFileTypes() throws IOException, FileUploadTypeException, CourseDoesNotExistException {
        
        DocFile badFile = new DocFile("app.exe", "My App", "user0", "app.exe", true);
        InputStream stream = Mockito.mock(FileInputStream.class);
        Uploader.handleUpload(badFile, stream);
        
    }
    
    private static void generateTxt() throws IOException {
        // txt file
        BufferedWriter writer = new BufferedWriter(new FileWriter("text1.txt"));
        writer.write("The dog runs fast.\n");
        writer.write("Cats don't like water.\n");
        writer.write("Elephants remember everything.");
        writer.close();
    }
    
    private static void removeFiles() {
        
        File text1 = new File("text1.txt");
        text1.delete();
        
        File docx1 = new File("docx1.docx");
        docx1.delete();

    }
    
}
