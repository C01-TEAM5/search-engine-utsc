package fall2018.cscc01.team5.searchEngineWebApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IndexHandlerSearchTest {
    
    /**
     * Set up the required txt, html, docx and pdf files.
     * Files are created and then added into the index.
     * 
     * @throws IOException
     * @throws DocumentException 
     */
    @BeforeClass
    public static void indexSetup() throws IOException {
        
        generateTxtFiles();
        generateHtmlFiles();
        generatePdfFiles();
        
    }
    
    @Test
    public void testSearchByType() {
        
    }
    
    /**
     * Remove the txt, html, docx and pdf files created
     * for testing.
     * 
     */
    @AfterClass
    public static void cleanUp() {
        
        removeFiles();
        
    }
    
    private static void generateTxtFiles() throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("text1.txt"));
        writer.write("The dog runs fast.\n");
        writer.write("Cats don't like water.\n");
        writer.write("Elephants remember everything.");
        writer.close();

        writer = new BufferedWriter(new FileWriter("text2.txt"));
        writer.write("When my computer runs, it makes a loud noise.\n");
        writer.write("It runs all day and all night, I should turn it off.");
        writer.close();
        
    }
    
    private static void generateHtmlFiles() throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("html1.html"));
        writer.write("<html>\n<head>My Baseball Team</head>\n<body>");
        writer.write("<h1>We are the best team in the league</h1>");
        writer.write("<a href=\"https://www.myteam.com\">See our team jersey!</a>");
        writer.write("<p>We are going to win the championship.</p>");
        writer.write("</body></html>");
        writer.close();
        
        writer = new BufferedWriter(new FileWriter("html2.html"));
        writer.write("<html>\n<head>Buy My New CD</head>\n<body>");
        writer.write("<h1>I am a great singer.</h1>");
        writer.write("<a href=\"https://www.catchy.com\">See me on stage</a>");
        writer.write("<img src=\"sing.gif\" alt=\"Sing\" height=\"50\" width=\"50\">");
        writer.write("<p>I hate baseball.</p>");
        writer.write("</body></html>");
        writer.close();
        
    }
    
    /**
     * Generate PDFs for testing.
     * Source: http://www.baeldung.com/java-pdf-creation
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
        contentStream.showText("Come to the trade show!");
        contentStream.showText("Monday to Friday from 9 AM to 6 PM");
        contentStream.endText();
        contentStream.close();
         
        pdf1.save("pdf1.pdf");
        pdf1.close();
        
        PDDocument pdf2 = new PDDocument();
        PDPage p2 = new PDPage();
        pdf2.addPage(p2);
         
        contentStream = new PDPageContentStream(pdf2, p2);
         
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        contentStream.showText("My Dog Spot");
        contentStream.showText("Here is my dog spot. He is missing.");
        contentStream.showText("Call my phone number if you find him.");
        contentStream.endText();
        contentStream.close();
         
        pdf2.save("pdf2.pdf");
        pdf2.close();
        
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
        
    }

}
