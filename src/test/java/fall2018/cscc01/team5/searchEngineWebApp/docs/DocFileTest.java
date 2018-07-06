package fall2018.cscc01.team5.searchEngineWebApp.docs;

import fall2018.cscc01.team5.searchEngineWebApp.document.DocFile;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DocFileTest {
	
    @Test
	public void testGetFileType () {
		
		DocFile testFile = new DocFile("hello","Hello","Chris","C:\\hello",false);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("run.txt","How To Run","Andrew","C:\\run.txt",true);
		assertEquals(true, testFile.getFileType().equals("txt"));
		
		testFile = new DocFile("","Nothing","Emily","C:\\",true);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("essay.","My Essay","Kalindu","C:\\essay.",true);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("outline.h","CSCC01 Course Outline","Abbas","C:\\outline.h",true);
		assertEquals(true, testFile.getFileType().equals("h"));
	}

}
