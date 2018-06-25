package fall2018.cscc01.team5.searchEngineWebApp.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;
import fall2018.cscc01.team5.searchEngineWebApp.handlers.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class IndexHandlerTest {
	
    private static IndexHandler indexHandler = null; 
    private static DocFile file = null;
    private static DocFile txtFile = null;
    private static DocFile htmlFile = null;
    private static DocFile pdfFile = null;
    private static DocFile docxFile = null;
    
    @Before
    public void init() {
        indexHandler = new IndexHandler("");
        
        try {
            generateTxt();
            generateHtml();
            generatePdf();
            generateDocx();
            
            
            // initial database
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void cleanUp() {
        indexHandler.closeWriter();
        removeFiles();
    }
    
    
    private static void generateTxt() throws IOException {
        // txt file
        BufferedWriter writer = new BufferedWriter(new FileWriter("text1.txt"));
        writer.write("The dog runs fast.\n");
        writer.write("Cats don't like water.\n");
        writer.write("Elephants remember everything.");
        writer.close();
        txtFile = new DocFile("text1.txt","Dog Story","Janice","text1.txt",true);
    }
    private static void generateHtml() throws IOException {
        // html file
        BufferedWriter writer = new BufferedWriter(new FileWriter("html1.html"));
        writer.write("<html>\n<head>Buy My New CD</head>\n<body>");
        writer.write("<h1>I am a great singer who doesn't like baseball.</h1>");
        writer.write("<a href=\"https://www.catchy.com\">See me on stage</a>");
        writer.write("<img src=\"sing.gif\" alt=\"Sing\" height=\"50\" width=\"50\">");
        writer.write("<p>I hate baseball.</p>");
        writer.write("</body></html>");
        writer.close();
        htmlFile = new DocFile("html1.html","Mark's CD","Mark","html1.html",false);
    }
    private static void generatePdf() throws IOException {        
        // pdf file
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        
        PDDocument pdf1 = new PDDocument();
        PDPage p1 = new PDPage();
        pdf1.addPage(p1);
         
        PDPageContentStream contentStream = new PDPageContentStream(pdf1, p1);
         
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        contentStream.showText("Come to the water trade show!");
        contentStream.showText("Monday to Friday from 9 AM to 6 PM. Free water.");
        contentStream.endText();
        contentStream.close();
         
        pdf1.save("pdf1.pdf");
        pdf1.close();
        pdfFile = new DocFile("pdf1.pdf","The Trade Show","Mark","pdf1.pdf",true); 
    }
    private static void generateDocx() throws IOException {
        // docx file
        XWPFDocument docx1 = new XWPFDocument();
        File loadFile = new File("docx1.docx");
        FileOutputStream stream = new FileOutputStream(loadFile);
        
        //Create new paragraph
        XWPFParagraph paragraph = docx1.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("My essay\n" + "Shakespeare writes very good books. My favourite" +
                " part of his stories is that they are all very different." + 
                " Some of his stories are sad and others are very happy and funny.");
        
        docx1.write(stream);
        stream.close();
        docxFile = new DocFile("docx1.docx","Shakespeare's Books","Alice","docx1.docx",true);  
    }
    
    private static void removeFiles() {
        
        File text1 = new File("text1.txt");
        text1.delete();

        File html1 = new File("html1.html");
        html1.delete();
        
        File pdf1 = new File("pdf1.pdf");
        pdf1.delete();

        File docx1 = new File("docx1.docx");
        docx1.delete();

    }
	
	
	// standard file types
    @Test
	public void testIndexHandlerAddTxt() {
		int expectedSize = search("*").size() + 1;
        indexHandler.addDoc(txtFile);
		
		// should successfully add the file and increase its size by 1
		assertEquals(expectedSize, search("*").size());
	}
    @Test
    public void testIndexHandlerUpdateTxt() {
        indexHandler.addDoc(txtFile);
        List<String> searchBefore = search("*");

        DocFile txtFile2 = new DocFile("text1.txt","Dog Story 2","Janice","text1.txt",true);
        indexHandler.updateDoc(txtFile2);
        List<String> searchAfter = search("*");
        
        // should successfully update the file and the lists should be different
        assertTrue(searchBefore.size() == searchAfter.size() && !searchAfter.equals(searchBefore));
    }
    @Test
    public void testIndexHandlerRemoveTxt() {
        indexHandler.addDoc(txtFile);
        int expectedSize = search("*").size() - 1;
        indexHandler.removeDoc(txtFile);

        // should successfully remove the file and decrease its size by 1
        assertEquals(expectedSize, search("*").size());   
    }
    
    
    @Test
    public void testIndexHandlerAddHtml() {
        int expectedSize = search("*").size() + 1;
        indexHandler.addDoc(htmlFile);
        
        // should successfully add the file and increase its size by 1
        assertEquals(expectedSize, search("*").size());
    }
    @Test
    public void testIndexHandlerUpdateHtml() {
        indexHandler.addDoc(htmlFile);
        List<String> searchBefore = search("*");

        DocFile htmlFile2 = new DocFile("html1.html","Mark's CD 2","Mark","html1.html",false);
        indexHandler.updateDoc(htmlFile2);
        List<String> searchAfter = search("*");
        
        // should successfully update the file and the lists should be different
        assertTrue(searchBefore.size() == searchAfter.size() && !searchAfter.equals(searchBefore));
    }
    @Test
    public void testIndexHandlerRemoveHtml() {
        indexHandler.addDoc(htmlFile);
        int expectedSize = search("*").size() - 1;
        indexHandler.removeDoc(htmlFile);

        // should successfully remove the file and decrease its size by 1
        assertEquals(expectedSize, search("*").size());   
    }
    
    
    @Test
    public void testIndexHandlerAddPdf() {
        int expectedSize = search("*").size() + 1;
        indexHandler.addDoc(pdfFile);
        
        // should successfully add the file and increase its size by 1
        assertEquals(expectedSize, search("*").size());
    }
    @Test
    public void testIndexHandlerUpdatePdf() {
        indexHandler.addDoc(pdfFile);
        List<String> searchBefore = search("*");

        DocFile pdfFile2 = new DocFile("pdf1.pdf","The Trade Show2","Mark","pdf1.pdf",true);
        indexHandler.updateDoc(pdfFile2);
        List<String> searchAfter = search("*");
        
        // should successfully update the file and the lists should be different
        assertTrue(searchBefore.size() == searchAfter.size() && !searchAfter.equals(searchBefore));
    }
    @Test
    public void testIndexHandlerRemovePdf() {
        indexHandler.addDoc(pdfFile);
        int expectedSize = search("*").size() - 1;
        indexHandler.removeDoc(pdfFile);

        // should successfully remove the file and decrease its size by 1
        assertEquals(expectedSize, search("*").size());   
    }
    
    
    
    @Test
    public void testIndexHandlerAddDocx() {
        int expectedSize = search("*").size() + 1;
        indexHandler.addDoc(docxFile);
        
        // should successfully add the file and increase its size by 1
        assertEquals(expectedSize, search("*").size());
    }
    @Test
    public void testIndexHandlerUpdateDocx() {
        indexHandler.addDoc(docxFile);
        List<String> searchBefore = search("*");

        DocFile docxFile2 = new DocFile("docx1.docx","Shakespeare's Books2","Alice","docx1.docx",true);
        indexHandler.updateDoc(docxFile2);
        List<String> searchAfter = search("*");
        
        // should successfully update the file and the lists should be different
        assertTrue(searchBefore.size() == searchAfter.size() && !searchAfter.equals(searchBefore));
    }
    @Test
    public void testIndexHandlerRemoveDocx() {
        indexHandler.addDoc(docxFile);
        int expectedSize = search("*").size() - 1;
        indexHandler.removeDoc(docxFile);

        // should successfully remove the file and decrease its size by 1
        assertEquals(expectedSize, search("*").size());   
    }
    
    
	// null parameter testing
    @Test
    public void testIndexHandlerAddNull() {
        int expectedSize = search("*").size();
        indexHandler.addDoc(null);
        
        // should not be able to add a null doc to the Index
        // size of search results should not change
        assertEquals(expectedSize, search("*").size());
    }
    @Test
    public void testIndexHandlerUpdateNull() {
        List<String> searchBefore = search("*");
        indexHandler.updateDoc(null);
        List<String> searchAfter = search("*");
        
        // should not be able to update a null doc in the Index
        // titled document should still be found with no changes
        assertTrue(searchAfter.equals(searchBefore));       
    }
    
    @Test
    public void testIndexHandlerRemoveNull() {
        int expectedSize = search("*").size();
        indexHandler.removeDoc(null);

        // should not be able to remove a null doc from the Index
        // size of search results should not change
        assertEquals(expectedSize, search("*").size());
    }
	
	
	// boundary cases
    /*
    @Test
	public void testIndexHandlerUpdateNonExisting() {
	    List<String> searchBefore = search("*");

        DocFile txtFile2 = new DocFile("text1.txt","Dog Story 2","Janice","text1.txt",true);
        indexHandler.updateDoc(txtFile2);
        List<String> searchAfter = search("*");

		// should not be able to update a non-existing doc in the Index
		// titled document should still be found with no changes
		assertTrue(searchBefore.equals(searchAfter));
	}
    */
	
	@Test
	public void testIndexHandlerRemoveEmpty() {
    	indexHandler.removeDoc(txtFile);
    	
    	// should not be able to remove a document from an empty db
    	assertEquals(0, search("*").size());
    }
    
    

	
	/*
	 * mock object to search the lucene db after each add/update/remove call
	 * base code is from : http://www.lucenetutorial.com/lucene-in-5-minutes.html
	 */
    private static List<String> search(String query) {

        
    	List<String> results = new ArrayList<String>();
    	
		try {
		    indexHandler.commitWriter();
			// create a query from user's query search
			QueryParser parser= new QueryParser(Constants.INDEX_KEY_TITLE, indexHandler.getAnalyzer());
			parser.setAllowLeadingWildcard(true);
			Query q = parser.parse(query);

			
			// get search results
	        int hitsPerPage = 10;
	        IndexReader reader = DirectoryReader.open(indexHandler.getRamIndex());
	        IndexSearcher searcher = new IndexSearcher(reader);
	        TopDocs docs = searcher.search(q, hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;

	        // store results
	        for(int i=0;i<hits.length;++i) {
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	            results.add(d.get(Constants.INDEX_KEY_TITLE));
	        }
	        
	        // close
	        reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}