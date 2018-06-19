package fall2018.cscc01.team5.searchEngineWebApp;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;
import junit.framework.TestCase;

public class IndexHandlerTest extends TestCase {

	public void testIsValidDocFile() {
		
		IndexHandler handler = new IndexHandler();
		
		//Test valid types
		
		DocFile testFile = new DocFile("hello.txt","Andrew",1,true);
		assertEquals(true,handler.isValidDocFile(testFile));
		
		testFile = new DocFile("hellothere.pdf","Andrew",1,true);
		assertEquals(true,handler.isValidDocFile(testFile));
		
		testFile = new DocFile("outline.html","Andrew",3,true);
		assertEquals(true,handler.isValidDocFile(testFile));
		
		testFile = new DocFile("a.txt","Andrew",100,true);
		assertEquals(true,handler.isValidDocFile(testFile));
		
		//Test invalid types
		testFile = new DocFile("hellothere.","Andrew",1,true);
		assertEquals(false,handler.isValidDocFile(testFile));
		
		testFile = new DocFile("","Andrew",1,true);
		assertEquals(false,handler.isValidDocFile(testFile));
		
		testFile = new DocFile("essay.doc","Andrew",1,true);
		assertEquals(false,handler.isValidDocFile(testFile));
		
	}
	
}
