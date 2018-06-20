package fall2018.cscc01.team5.searchEngineWebApp;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import junit.framework.TestCase;

public class DocFileTest extends TestCase {
	
	public void testGetFileType () {
		
		DocFile testFile = new DocFile("hello","Chris","C:\\hello",false);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("run.txt","Andrew","C:\\run.txt",true);
		assertEquals(true, testFile.getFileType().equals("txt"));
		
		testFile = new DocFile("","Emily","C:\\",true);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("essay.","Kalindu","C:\\essay.",true);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("outline.h","Abbas","C:\\outline.h",true);
		assertEquals(true, testFile.getFileType().equals("h"));
	}

}
