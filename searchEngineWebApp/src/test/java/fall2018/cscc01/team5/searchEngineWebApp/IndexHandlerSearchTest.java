package fall2018.cscc01.team5.searchEngineWebApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class IndexHandlerSearchTest {
    
    /**
     * Set up the required txt, html, docx and pdf files.
     * Files are created and then added into the index.
     * 
     * @throws IOException
     */
    @BeforeClass
    public static void indexSetup() throws IOException {
        
        generateTxtFiles();
        generateHtmlFiles();
        
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
    
    private static void generatePdfFiles() throws IOException {
        
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
        
    }

}
