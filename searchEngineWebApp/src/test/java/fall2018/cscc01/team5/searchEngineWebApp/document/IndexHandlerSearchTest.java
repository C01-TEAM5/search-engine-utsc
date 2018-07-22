package fall2018.cscc01.team5.searchEngineWebApp.document;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.dom4j.DocumentException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fall2018.cscc01.team5.searchEngineWebApp.document.DocFile;

public class IndexHandlerSearchTest {
    
    private static IndexHandler index = null;
    private static ArrayList<DocFile> docFiles = new ArrayList<DocFile>();
    public static final int TXT1 = 0;
    public static final int TXT2 = 1;
    public static final int HTML1 = 2;
    public static final int HTML2 = 3;
    public static final int PDF1 = 4;
    public static final int PDF2 = 5;
    public static final int DOCX1 = 6;
    public static final int DOCX2 = 7;
    
    /**
     * Set up the required txt, html, docx and pdf files.
     * Files are created and then added into the index.
     * 
     * @throws IOException
     * @throws DocumentException 
     */
    @BeforeClass
    public static void indexSetup() throws IOException, DocumentException {

        index = IndexHandler.getInstance();

        generateTxtFiles();
        generateHtmlFiles();
        generatePdfFiles();
        generateDocxFiles();
        
        for (DocFile docFile:docFiles) {
            index.addDoc(docFile);
        }
        
        
    }

    //Test searching across all doc types with no permission set
    //Test correct number of results returned and in right order
    @Test
    public void testContentSearch() throws ParseException, IOException {
    	
    	String [] filters = {".txt",".pdf",".html",".docx"};
    	DocFile[] results = index.search("baseball", 0, filters);
    	
    	assertEquals(3,results.length);
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(HTML2)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(TXT2)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(HTML1)));
    	
    }
    
    @Test
    public void testContentSearchOrder() throws ParseException, IOException {
    	
    	String [] filters = {".txt",".pdf",".html",".docx"};
    	DocFile[] results = index.search("baseball", 0, filters);
    	
    	assertEquals(3,results.length);
    	assertEquals(docFiles.get(HTML2),results[0]);
    	assertEquals(docFiles.get(HTML1),results[1]);
    	assertEquals(docFiles.get(TXT2),results[2]);
    	
    }
    
    //Confirm that search checks titles
    @Test
    public void testTitleSearch() throws ParseException, IOException{
    	
    	String [] filters = {".docx"};
    	DocFile[] results = index.search("running", 0, filters);
    	
    	assertEquals(1,results.length);
    	assertEquals(docFiles.get(DOCX2),results[0]);
    	
    }
    
    @Test
    public void testNoResults() throws ParseException, IOException {
    	
    	String [] filters = {".txt",".pdf",".html",".docx"};
    	DocFile[] results = index.search("supercalifragulistic", 0, filters);
    	
    	assertEquals(0, results.length);
    }
    
    //Test searching across all doc types with multiple word query
    @Test
    public void testMultipleWordQuery() throws ParseException, IOException {
    	
    	String [] filters = {".txt",".pdf",".html",".docx"};
    	DocFile[] results = index.search("dog runs", 0, filters);
    	
    	assertEquals(4, results.length);
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(TXT1)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(TXT2)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(HTML1)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(PDF2)));
    	
    }
    
    //Test ordering of top search results
    @Test
    public void testSearchOrdering() throws ParseException, IOException {
    	
    	String [] filters = {".txt",".pdf",".html",".docx"};
    	DocFile[] results = index.search("dog runs", 0, filters);
    	
    	assertEquals(4, results.length);
    	assertEquals(docFiles.get(TXT1),results[0]);
    	assertEquals(docFiles.get(PDF2),results[1]);
    	
    }
    
    //Test that changing the filter type changes the results
    //to only include the specified document type
    @Test
    public void testTypeFilter () throws ParseException, IOException {
    	
    	String [] filters = {".txt"};
    	DocFile[] results = index.search("water", 0, filters);
    	
    	assertEquals(1, results.length);
    	assertEquals(docFiles.get(TXT1),results[0]);
    	
    	String[] filters2 = {".pdf"};
    	results = index.search("water", 0, filters2);
    	
    	assertEquals(1, results.length);
    	assertEquals(docFiles.get(PDF1),results[0]);
    	
    	String[] filters3 = {".docx"};
    	results = index.search("water", 0, filters3);
    	
    	assertEquals(0, results.length);
    	
    }
    
    //Test multiple filters active at once
    @Test
    public void testMultipleFilters () throws ParseException, IOException {
    	
    	String[] filters = {".docx", ".pdf"};
    	DocFile[] results = index.search("shakespeare", 0, filters);
    	
    	assertEquals(2, results.length);
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(PDF2)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(DOCX1)));   	
    	
    }
    
    //Test a search on all files with a certain level of permission
    @Test
    public void testPermissionSearch() throws ParseException, IOException {
    	
    	String[] filters = {".docx",".pdf",".html",".txt"};
    	DocFile[] results = index.search("*", Constants.PERMISSION_INSTRUCTOR, filters);
    	
    	assertEquals(3, results.length);
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(TXT1)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(TXT2)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(DOCX1)));
    	
    	results = index.search("*", Constants.PERMISSION_STUDENT, filters);
    	assertEquals(2, results.length);
    }
    
    //Test searching for a valid search ID
    @Test
    public void testSearchById () throws ParseException, IOException {
    	
    	String[] filters = {".docx",".pdf",".html",".txt"};
    	DocFile[] results = index.searchById("test", filters);
    	
    	assertEquals(docFiles.get(TXT1),results[0]);
    	
    	results = index.searchById("testpdf", filters);
    	assertEquals(docFiles.get(PDF1),results[0]);   	  	
    }
    
    //Test searching for an invalid search ID
    @Test
    public void testInvalidId () throws ParseException, IOException {
    	
    	String[] filters = {".docx",".pdf",".html",".txt"};
    	DocFile[] results = index.searchById("test123", filters);
    	
    	assertEquals(0,results.length);
    	
    }
    
    //Test searching by User
    @Test
    public void testSearchByUser() throws ParseException, IOException {
    	
    	String[] filters = {".docx",".pdf",".html",".txt"};
    	DocFile[] results = index.searchByUser("Mark", filters);
    	
    	assertEquals(2,results.length);
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(HTML1)));
    	assertEquals(true, Arrays.asList(results).contains(docFiles.get(PDF1)));
    	
    }
    
    /**
     * Remove the txt, html, docx and pdf files created
     * for testing.
     * 
     */
    @AfterClass
    public static void cleanUp() throws ParseException {
        for (DocFile file: docFiles) {
            index.removeDoc(file);
        }

        index.closeWriter();
        removeFiles();
        
    }
    
    private static void generateTxtFiles() throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("text1.txt"));
        writer.write("The dog runs fast.\n");
        writer.write("Cats don't like water.\n");
        writer.write("Elephants remember everything.");
        writer.close();
        DocFile txt1 = new DocFile("text1.txt","Dog Story","Janice","text1.txt",true);
        txt1.setPermissions(Constants.PERMISSION_INSTRUCTOR);
        txt1.setId("test");
        docFiles.add(txt1);
        
        writer = new BufferedWriter(new FileWriter("text2.txt"));
        writer.write("When my computer runs, it makes a loud noise.\n");
        writer.write("It runs all day and all night, I should turn it off.");
        writer.close();
        DocFile txt2 = new DocFile("text2.txt","Baseball Story","Adam","text2.txt",false);
        txt2.setPermissions(Constants.PERMISSION_INSTRUCTOR);
        txt2.setId("1");
        docFiles.add(txt2);
        
    }
    
    private static void generateHtmlFiles() throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("html1.html"));
        writer.write("<html>\n<head>Buy My New CD Shakespeare</head>\n<body>");
        writer.write("<h1>I am a great singer who doesn't like baseball but has a dog.</h1>");
        writer.write("<a href=\"https://www.catchy.com\">See me on stage</a>");
        writer.write("<img src=\"sing.gif\" alt=\"Sing\" height=\"50\" width=\"50\">");
        writer.write("<p>I hate baseball.</p>");
        writer.write("</body></html>");
        writer.close();
        DocFile html1 = new DocFile("html1.html","Mark CD","Mark","html1.html",false);
        html1.setPermissions(Constants.PERMISSION_STUDENT);
        html1.setId("2");
        docFiles.add(html1);
        
        writer = new BufferedWriter(new FileWriter("html2.html"));
        writer.write("<html>\n<head>My Baseball Team</head>\n<body>");
        writer.write("<h1>We are the best baseball team in the league</h1>");
        writer.write("<a href=\"https://www.myteam.com\">See our team jersey!</a>");
        writer.write("<p>We are going to win the championship.</p>");
        writer.write("</body></html>");
        writer.close();
        DocFile html2 = new DocFile("html2.html","My Baseball Team","Naomi","html2.html",true); 
        html2.setId("3");
        docFiles.add(html2);
        
    }
    
    /**
     * Generate PDFs for testing.
     * Source for learning: http://www.baeldung.com/java-pdf-creation
     * 
     * @throws IOException
     */
    private static void generatePdfFiles() throws IOException {
        
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
        DocFile pf1 = new DocFile("pdf1.pdf","The Trade Show","Mark","pdf1.pdf",true);
        pf1.setPermissions(Constants.PERMISSION_STUDENT);
        pf1.setId("testpdf");
        docFiles.add(pf1);
        
        PDDocument pdf2 = new PDDocument();
        PDPage p2 = new PDPage();
        pdf2.addPage(p2);
         
        contentStream = new PDPageContentStream(pdf2, p2);
         
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        contentStream.showText("My Dog");
        contentStream.showText("Here is my dog Shakespeare. He is a missing dog.");
        contentStream.showText("Call my phone number runs if you find him. I miss my dog.");
        contentStream.endText();
        contentStream.close();
         
        pdf2.save("pdf2.pdf");
        pdf2.close();
        DocFile pd2 = new DocFile("pdf2.pdf","Shakes Lost","Jane","pdf2.pdf",true);
        pd2.setId("10");
        docFiles.add(pd2);
        
    }
    
    /**
     * Create new docx files for testing.
     * 
     * Source for learning:
     * https://www.tutorialspoint.com/apache_poi_word/apache_poi_word_quick_guide.htm
     * 
     * @throws IOException
     */
    private static void generateDocxFiles() throws IOException {

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
        DocFile doc1 = new DocFile("docx1.docx","Shakespeare's Books","Alice","docx1.docx",true);
        doc1.setPermissions(Constants.PERMISSION_INSTRUCTOR);
        doc1.setId("ok");
        docFiles.add(doc1);
        
        XWPFDocument docx2 = new XWPFDocument();
        loadFile = new File("docx2.docx");
        stream = new FileOutputStream(loadFile);
        
        //Create new paragraph
        paragraph = docx2.createParagraph();
        run = paragraph.createRun();
        run.setText("This is a story about when I got lost in the forest." +
                "I ran as fast as I could but I didn't know where to go" +
                "Luckily I found a highway and was able to walk back home afterwards." +
                "Run run run run run. I run fast.");
        
        docx2.write(stream);
        stream.close();
        DocFile doc2 = new DocFile("docx2.docx","My Running Story","Adam","docx2.docx",true); 
        doc2.setId("hello");
        docFiles.add(doc2);
    }
    
    private static void removeFiles() {
               
        File text1 = new File("text1.txt");
        text1.delete();

        File text2 = new File("text2.txt");
        text2.delete();
        
        File html1 = new File("html1.html");
        html1.delete();
        
        File html2 = new File("html2.html");
        html2.delete();
        
        File pdf1 = new File("pdf1.pdf");
        pdf1.delete();
        
        File pdf2 = new File("pdf2.pdf");
        pdf2.delete();
        
        File docx1 = new File("docx1.docx");
        docx1.delete();
        
        File docx2 = new File("docx2.docx");
        docx2.delete();
        
    }

}
