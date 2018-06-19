package fall2018.cscc01.team5.searchEngineWebApp;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import junit.framework.TestCase;

public class DocFileTest extends TestCase {
	
	public void testGetFileType () {
		
		DocFile testFile = new DocFile("hello","Chris",1,false);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("run.txt","Andrew",2,true);
		assertEquals(true, testFile.getFileType().equals("txt"));
		
		testFile = new DocFile("","Emily",2,true);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("essay.","Kalindu",2,true);
		assertEquals(true, testFile.getFileType().equals(""));
		
		testFile = new DocFile("outline.h","Abbas",2,true);
		assertEquals(true, testFile.getFileType().equals("h"));
	}

}
