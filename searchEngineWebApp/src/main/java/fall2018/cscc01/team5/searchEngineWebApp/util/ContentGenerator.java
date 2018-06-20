package fall2018.cscc01.team5.searchEngineWebApp.util;

import org.apache.lucene.document.Document;

import fall2018.cscc01.team5.searchEngineWebApp.docs.DocFile;

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

        if (fileType.equals("pdf")) {
            generatePdf(doc, file);
        } else if (fileType.equals("txt")) {
            generateTxt(doc, file);
        } else if (fileType.equals("html")) {
            generateHtml(doc, file);
        } else if (fileType.equals("docx")) {
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

    }

    /**
     * Generates the content field for a HTML DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generateHtml (Document doc, DocFile file) {

    
    }

    /**
     * Generates the content field for a TXT DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generateTxt (Document doc, DocFile file) {

    }

    /**
     * Generates the content field for a Docx DocFile.
     *
     * @param doc  the Document where the field is being added
     * @param file the DocFile whose content is being added
     */
    private static void generateDocx (Document doc, DocFile file) {

    }

}
