package fall2018.cscc01.team5.searchEngineWebApp.document;

import static org.junit.Assert.assertArrayEquals;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import fall2018.cscc01.team5.searchEngineWebApp.document.IndexHandler;
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
    
    /**
     * Test method to test out different types of content search.
     * 
     * @throws ParseException
     * @throws IOException 
     */
    @Test
    public void testContentSearch() throws ParseException, IOException {
                
        //Test for content found in only one file
        String[] query = {"run"};
        String[] filter = {"Content"};
        DocFile [] actualFiles = index.search(query, filter, false);
        DocFile [] expectedFiles = {docFiles.get(DOCX2)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test empty search
        query = new String[] {""};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test for content found in multiple files
        //Also checks case sensitivity
        //Ensure correct order
        query = new String[] {"baseball"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[]{docFiles.get(HTML2), docFiles.get(HTML1)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test for content across multiple different file types
        //Ensure correct order
        query = new String[] {"water"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {docFiles.get(PDF1),docFiles.get(TXT1)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test for content that doesn't exist in any files
        query = new String[] {"supercalifragulistic"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Check to make sure HTML files are stripped properly
        query = new String[] {"html", "h1", "a href"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Check behaviour when multiple queries searched at once
        query = new String[] {"dog", "cats"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {docFiles.get(TXT1),docFiles.get(PDF2),docFiles.get(HTML1)};
        assertArrayEquals(expectedFiles,actualFiles);
    }
    
    /**
     * Testing the title search functionality.
     * @throws ParseException 
     * 
     */
    @Test
    public void testTypeSearch() throws ParseException, IOException {
        //Test searching an invalid type
        String[] query = {".hello"};
        String[] filter = {"Type"};
        DocFile [] actualFiles = index.search(query, filter, true);
        DocFile [] expectedFiles = {};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test searching on txt
        query = new String[]{".txt"};
        filter = new String[]{"Type"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[]{docFiles.get(TXT1),docFiles.get(TXT2)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test searching on html & pdf
        query = new String[]{".html", ".pdf"};
        filter = new String[]{"Type"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[]{docFiles.get(HTML1),docFiles.get(HTML2),docFiles.get(PDF1),docFiles.get(PDF2)};
        System.out.println("\n\n\n\n\n\n\n");
        System.out.println(Arrays.toString(actualFiles));
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Test searching on docx
        query = new String[]{".docx"};
        filter = new String[]{"Type"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[]{docFiles.get(DOCX1),docFiles.get(DOCX2)};
        assertArrayEquals(expectedFiles,actualFiles);
        
    }
    
    
    /**
     * Test method to test searching across multiple filters at once.
     * @throws ParseException 
     */
    @Test
    public void testMultipleFilterSearch() throws ParseException, IOException {
        
        //Look for instances of baseball being in Content and Title fields
        String[] query = {"baseball"};
        String[] filter = {"Content", "Title"};
        DocFile [] actualFiles = index.search(query, filter, false);
        DocFile [] expectedFiles = {docFiles.get(HTML2)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Look for Elephants in both body and title (only happens in body)
        query = new String[] {"elephants"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Look for Spot in both body and title (only happens in title)
        query = new String[] {"lost"};
        actualFiles = index.search(query, filter, false);
        expectedFiles = new DocFile[] {};
        assertArrayEquals(expectedFiles,actualFiles);
        
    }
    
    /**
     * Test method to test expanded search.
     * @throws ParseException 
     * 
     */
    @Test
    public void testExpandedSearch() throws ParseException, IOException {
        //Look for instances of baseball being in Content or Title fields
        String[] query = {"baseball"};
        String[] filter = {"Content", "Title"};
        DocFile [] actualFiles = index.search(query, filter, true);
        DocFile [] expectedFiles = {docFiles.get(HTML2),docFiles.get(HTML1),docFiles.get(TXT2)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Look for instances of baseball and monday being in Content or Title fields
        query = new String[] {"baseball","monday"};
        filter = new String[] {"Content", "Title"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[] {docFiles.get(HTML2),docFiles.get(PDF1),docFiles.get(HTML1),docFiles.get(TXT2)};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Check case when Query is in Two of the three filters
        query = new String[] {"mark"};
        filter = new String[] {"Content", "Title", "Owner"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[] {docFiles.get(HTML1),docFiles.get(PDF1)};
        assertArrayEquals(expectedFiles,actualFiles);

        //Check case when query is not in either filter
        query = new String[] {"alice"};
        filter = new String[] {"Content", "Title"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[] {};
        assertArrayEquals(expectedFiles,actualFiles);
        
        //Check case when query is in the final filter
        query = new String[] {"alice"};
        filter = new String[] {"Content", "Title", "Owner"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[] {docFiles.get(DOCX1)};
        assertArrayEquals(expectedFiles,actualFiles);
    }
    
    /**
     * Test method to test expanded search.
     * @throws ParseException 
     * 
     */
    @Test
    public void testPathSearch() throws ParseException, IOException {
        
        String[] query = {"*html2.html"};
        String[] filter = {"Path"};
        DocFile [] actualFiles = index.search(query, filter, true);
        DocFile [] expectedFiles = {docFiles.get(HTML2)};
        assertArrayEquals(expectedFiles,actualFiles);

        query = new String[]{"*html2.html", "*pdf1.pdf"};
        filter = new String[]{"Path"};
        actualFiles = index.search(query, filter, true);
        expectedFiles = new DocFile[]{docFiles.get(HTML2),docFiles.get(PDF1)};
        assertArrayEquals(expectedFiles,actualFiles);
        
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
        docFiles.add(new DocFile("text1.txt","Dog Story","Janice","text1.txt",true));
        
        writer = new BufferedWriter(new FileWriter("text2.txt"));
        writer.write("When my computer runs, it makes a loud noise.\n");
        writer.write("It runs all day and all night, I should turn it off.");
        writer.close();
        docFiles.add(new DocFile("text2.txt","Baseball Story","Adam","text2.txt",false));
        
    }
    
    private static void generateHtmlFiles() throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("html1.html"));
        writer.write("<html>\n<head>Buy My New CD</head>\n<body>");
        writer.write("<h1>I am a great singer who doesn't like baseball but has a dog.</h1>");
        writer.write("<a href=\"https://www.catchy.com\">See me on stage</a>");
        writer.write("<img src=\"sing.gif\" alt=\"Sing\" height=\"50\" width=\"50\">");
        writer.write("<p>I hate baseball.</p>");
        writer.write("</body></html>");
        writer.close();
        docFiles.add(new DocFile("html1.html","Mark CD","Mark","html1.html",false));
        
        writer = new BufferedWriter(new FileWriter("html2.html"));
        writer.write("<html>\n<head>My Baseball Team</head>\n<body>");
        writer.write("<h1>We are the best baseball team in the league</h1>");
        writer.write("<a href=\"https://www.myteam.com\">See our team jersey!</a>");
        writer.write("<p>We are going to win the championship.</p>");
        writer.write("</body></html>");
        writer.close();
        docFiles.add(new DocFile("html2.html","My Baseball Team","Naomi","html2.html",true));
        
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
        docFiles.add(new DocFile("pdf1.pdf","The Trade Show","Mark","pdf1.pdf",true));
        
        PDDocument pdf2 = new PDDocument();
        PDPage p2 = new PDPage();
        pdf2.addPage(p2);
         
        contentStream = new PDPageContentStream(pdf2, p2);
         
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        contentStream.showText("My Dog Spot");
        contentStream.showText("Here is my dog spot. He is a missing dog.");
        contentStream.showText("Call my phone number if you find him. I miss my dog.");
        contentStream.endText();
        contentStream.close();
         
        pdf2.save("pdf2.pdf");
        pdf2.close();
        docFiles.add(new DocFile("pdf2.pdf","Spot Lost","Jane","pdf2.pdf",true));
        
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
        docFiles.add(new DocFile("docx1.docx","Shakespeare's Books","Alice","docx1.docx",true));
        
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
        docFiles.add(new DocFile("docx2.docx","My Running Story","Adam","docx2.docx",true));
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
