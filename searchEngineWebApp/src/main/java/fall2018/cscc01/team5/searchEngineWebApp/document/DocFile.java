package fall2018.cscc01.team5.searchEngineWebApp.document;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DocFile {

    private String id;
    private String filename; //Name of the file
    private boolean isPublic; //Whether the document can be accessed publicly
    private String courseCode; //Optional Course code association
    private String owner; //Uploader of the file
    private String path; //ID number of the file for updating/deletion
    private String title; //The title of the Document
    private String fileType;
    private int permission;
    private List<String> tags;

    /**
     * A DocFile is an object representing a file in the system. DocFiles contain a filename, is uploaded by a user, can
     * be private or public depending on user preferences and can be connected to certain UTSC Courses.
     */
    public DocFile (String filename, String title, String owner, String path, boolean isPublic) {

        this.id = UUID.randomUUID().toString().replaceAll("-", "");

        this.filename = filename;
        this.isPublic = isPublic;
        this.courseCode = "";
        this.owner = owner;
        this.path = path;
        this.title = title;
        this.fileType = getFileType();
        permission = Constants.PERMISSION_ALL;
        this.tags = new ArrayList<String>();
    }

    /**
     * Returns a string representation of the file type. The string representation is all characters after the final '.'
     * in the path. If there is no '.' in the filename, an empty string is returned.
     *
     * @return String filetype of the DocFile
     */
    public String getFileType () {

        int dotIndex = filename.lastIndexOf('.') + 1;
        String fileType = "";

        //if there is a . in the filename, get everything after it
        if (dotIndex > 0) {
            fileType = filename.substring(dotIndex);

        }

        return fileType;
    }

    /**
     * Return the full path of this DocFile
     *
     * @return the full path of this DocFile
     */
    public String getPath () {
        return path;
    }

    /**
     * Return the owner of this DocFile
     *
     * @return the owner of this DocFile
     */
    public String getOwner () {
        return owner;
    }

    /**
     * Return the filename of this DocFile
     *
     * @return the filename of this DocFile
     */
    public String getFilename () {
        return filename;
    }

    /**
     * Return the public status of this DocFile
     *
     * @return the public status of this DocFile
     */
    public Boolean isPublic () {
        return isPublic;
    }

    /**
     * Return the title of this DocFile
     *
     * @return the title of this DocFile
     */
    public String getTitle () {
        return title;
    }

    /**
     * Return the Course code of this DocFile
     *
     * @return the Course code of this DocFile
     */
    public String getCourseCode () {
        return courseCode;
    }

    /**
     * Return the permission attached to this User.
     * @return a permission attached to this User
     */
    public int getPermission() {
        return this.permission;
    }

    /**
     * Replace this Users permission.
     * @param perm - the replacement permission
     */
    public void setPermissions(int perm) {
        this.permission = perm;
    }
    
    /**
     * Return the tags attached to the docfile
     * @return a list of tags attached to the docfile
     */
    public List getTags() {
      return this.tags;
    }
    
    /**
     * Add a new tag to the document
     * @param tag - the tag to be added
     */
    public void addTag(String tag) {
      this.tags.add(tag);
    }
    
    /**
     * Sets new tags to the document
     * @param tags - the tags to be set
     */
    public void setTag(List<String> tags) {
      this.tags = tags;
    }
    
    /**
     * flatten a list of tags to string
     * @return a string of all the tags
     */
    public String flattenTags() {
      String result = "";
      if (tags.size() > 0) {
        for (String tag : tags) {
          result = result + tag + ", ";
        }
        result = result.substring(0, result.length() - 2);
      }
      return result;
    }
    
    /**
     * Check whether an object is equivalent to this DocFile
     *
     * @param obj object to compare with this DocFile
     * @return a true if obj is equivalent to this DocFile, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        try {
            DocFile toComapre = (DocFile) obj;
            return (this.isPublic == toComapre.isPublic()) && (this.filename.equals(toComapre.getFilename())) &&
                    (this.path.equals(toComapre.getPath())) && (this.courseCode.equals(toComapre.getCourseCode())) &&
                    (this.owner.equals(toComapre.getOwner())) && (this.title.equals(toComapre.getTitle()));
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Get the id of this file
     * 
     * @return the id of this file
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * Set the id of this file
     * @param id the new id of this file
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Set the path of this file
     * @param path the new path of this file
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
     * Change the course code this file belongs to.
     * @param code the new course code
     */
    public void setCourseCode(String code) {
        this.courseCode = code;
    }
    
    /**
     * Return an integer representation of this DocFile
     *
     * @return an integer representation of this DocFile
     */
    @Override
    public int hashCode () {
        return Objects.hash(isPublic, filename, path, courseCode, owner, title);
    }

    /**
     * Return a string representation of this DocFile
     *
     * @return a string representation ofthis DocFile
     */
    @Override
    public String toString () {

        String result = "DocFile [" +
                "filename: " + filename +
                ", owner: " + owner +
                ", title: " + title +
                ", path: " + path +
                ", Course code: " + courseCode +
                ", public status: " + isPublic +
                ", Permission: " + permission + 
                "]";

        return result;
    }
}
