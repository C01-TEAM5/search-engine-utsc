package fall2018.cscc01.team5.searchEngineWebApp.document;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import fall2018.cscc01.team5.searchEngineWebApp.document.DocFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Utility class designed to generate content fields and add them to Documents that are being added to the index.
 * <p>
 * This class is responsible for parsing the required document and adding it to the existing Document object.
 */
public class ContentGenerator {

    /**
     * The primary method that is called to generate a content field. This method determines the type of file, parses it
     * appropriately and adds the content of the file to a field in the Document with the title "Content".
     * <p>
     * Precondition (checked in IndexHandler): fileType must be either pdf, txt, html or docx.
     *
     * @param doc  the Document where the field is to be added
     * @param file the DocFile whose content is being added
     */
    public static void generateContent (Document doc, DocFile file) {

        String fileType = file.getFileType();

        if (fileType.equals(Constants.FILETYPE_PDF)) {
            generatePdf(doc, file);
        } else if (fileType.equals(Constants.FILETYPE_TXT)) {
            generateTxt(doc, file);
        } else if (fileType.equals(Constants.FILETYPE_HTML)) {
            generateHtml(doc, file);
        } else if (fileType.equals(Constants.FILETYPE_DOCX)) {
            generateDocx(doc, file);
        }
    }

    /**
     * Generates the content field for a PDF DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generatePdf (Document doc, DocFile file) {

        //Required for use with JDK 8
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        String pdfContents = "";
        PDDocument pdfDoc = null;
        String filePath = file.getPath();
        File pdfFile = new File(filePath);

        try {
            pdfDoc = PDDocument.load(pdfFile);
            PDFTextStripper strip = new PDFTextStripper();
            pdfContents = strip.getText(pdfDoc);
            addContent(doc, pdfContents);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdfDoc != null) {
                try {
                    pdfDoc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Generates the content field for a HTML DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generateHtml (Document doc, DocFile file) {

        // create a file object using the file path
        File input = new File(file.getPath());
        // Parse the file using the Jsoup parser
        org.jsoup.nodes.Document parsedDoc = null;
        try {
            parsedDoc = Jsoup.parse(input, "UTF-8", "");
            // index title of the document (for now using content key)
            addContent(doc, parsedDoc.title());
            // index all other text
            for (Element element : parsedDoc.select("*")) {
                // add "value" and "text" attributed of each tag to the index if they are not empty
                String eleValue = element.attr("value");
                String eleText = element.text();
                if (!eleValue.equals(""))
                    addContent(doc, eleValue);
                if (!eleText.equals(""))
                    addContent(doc, eleText);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Generates the content field for a TXT DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generateTxt (Document doc, DocFile file) {

        String filePath = file.getPath();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        BufferedReader BufferedFileReader = new BufferedReader(fileReader);
        String lineContent = null;
        try {
            while ((lineContent = BufferedFileReader.readLine()) != null) {
                addContent(doc, lineContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileReader.close();
            BufferedFileReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * Generates the content field for a Docx DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generateDocx (Document doc, DocFile file) {

        String filePath = file.getPath();

        try {

            FileInputStream fileInputStream = new FileInputStream(filePath);
            XWPFDocument document = new XWPFDocument(fileInputStream);
            // includes only body text (i.e paragraphs and tables)
            List<IBodyElement> bodyElement = document.getBodyElements();
            for (IBodyElement element : bodyElement) {

                // for body text
                if (element instanceof XWPFParagraph) {
                    addContent(doc,((XWPFParagraph) element).getText());

                    // for body tables
                } else if (element instanceof XWPFTable) {
                    addContent(doc,((XWPFTable) element).getText());
                }
            }

            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper function to addContent to the document type no matter what the extension is.
     * Adds content in a way that it can later be highlighted by search results.
     * 
     * @param doc
     * @param contents
     */
    private static void addContent (Document doc, String content) {
        
        FieldType addType = new FieldType(TextField.TYPE_STORED);
        addType.setStoreTermVectors(true);
        addType.setStoreTermVectorPositions(true);
        addType.setStoreTermVectorOffsets(true);
        doc.add(new Field(Constants.INDEX_KEY_CONTENT, content, addType));
        
    }
}
