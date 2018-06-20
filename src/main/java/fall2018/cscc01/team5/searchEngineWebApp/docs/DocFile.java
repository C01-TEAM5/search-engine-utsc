package fall2018.cscc01.team5.searchEngineWebApp.docs;

public class DocFile {
	
	private String filename; //Name of the file
	private boolean isPublic; //Whether the document can be accessed publicly
	private String courseCode; //Optional course code association
	private String owner; //Uploader of the file
	private String path; //ID number of the file for updating/deletion
	
	/** 
	 * A DocFile is an object representing a file in the system.
	 * DocFiles contain a filename, is uploaded by a user, can be
	 * private or public depending on user preferences and can
	 * be connected to certain UTSC Courses.
	 * 
	 */
	public DocFile(String filename, String owner, String path, boolean isPublic) {
		
		this.filename = filename;
		this.isPublic = isPublic;
		this.courseCode = null;
		this.owner = owner;
		this.path = path;
	}
	
	/**
	 * Returns a string representation of the file type.
	 * The string representation is all characters after the final '.' in the path.
	 * If there is no '.' in the filename, an empty string is returned.
	 * 
	 * @return String filetype of the DocFile
	 */
	public String getFileType() {
		
		int dotIndex = filename.lastIndexOf('.')+1;
		String fileType = "";
		
		//if there is a . in the filename, get everything after it
		if (dotIndex > 0) {
			fileType = filename.substring(dotIndex);
			
		}
		
		return fileType;
	}

	public String getPath() {
		return path;
	}

	public String getOwner() {
		return owner;
	}

}
