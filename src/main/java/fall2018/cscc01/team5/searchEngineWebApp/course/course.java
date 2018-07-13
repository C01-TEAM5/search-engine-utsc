package fall2018.cscc01.team5.searchEngineWebApp.course;

import java.util.ArrayList;

import fall2018.cscc01.team5.searchEngineWebApp.user.User;


public class course {
	
	private String name;
	private String code;
	private int size;
	
	public course(String courseName, String courseCode, int courseSize) {
		this.name = courseName;
		this.code = courseCode;
		this.size = courseSize;
	}
	
	/**
	 * Get all students in the course.
	 * 
	 * @return an list contains all students.
	 */
	public ArrayList<User> getAllStudents() {
		return null;
	}
	
	/**
	 * Get all TAs in thr course.
	 * 
	 * @return an list contains all TAs.
	 */
	public ArrayList<User> getAllTAs() {
		return null;
	}
	
	/**
	 * Get all instructors in the course.
	 * 
	 * @return an list contains all instructors. 
	 */
	public ArrayList<User> getAllInstructors() {
		return null;
	}
	
	/**
	 * Get all users including instructors, TAs, and students in the course.
	 * 
	 * @return an list contains all users in the course. 
	 */
	public ArrayList<User> getAllUsers() {
		return null;
	}
	
	/**
	 * Add a student in the course.
	 * 
	 * @param studentName 
	 * @param username
	 * @return True if the student is added or student is already in course record. otherwise false.
	 */
	public Boolean addStudent(String studentName, String username) {
		return false;
	}
	
	/**
	 * Remove a student in the course.
	 * 
	 * @param username
	 * @return true if remove success. otherwise false.
	 */
	public Boolean removeStudent(String username) {
		return false;
	}
	
	/**
	 * Check if the student is in the course. 
	 * 
	 * @param username
	 * @return true if student in the course. otherwise false.
	 */
	public Boolean studentInCourse(String username) {
		return false;
	}
	
	/**
	 * Add a TA in the course.
	 * @param TAname
	 * @param username
	 * @return return true if TA is added to course, or TA is already in course. Otherwise false.
	 */
	public Boolean addTA(String TAname, String username) {
		return false;
	}
	
	/**
	 * Remove a TA from the course.
	 * 
	 * @param username
	 * @return true if the TA is removed. Otherwise false.
	 */
	public Boolean removeTA(String username) {
		return false;
	}
	
	/**
	 * Check if TA is in the course.
	 * 
	 * @param username
	 * @return True if TA is in the course. Otherwise false. 
	 */
	public Boolean TAinCourse(String username) {
		return false;
	}
	
	/**
	 * Add an instructor in the course. 
	 * 
	 * @param instructorName
	 * @param username
	 * @return true if the instructor is added, or instructor is already in course list. otherwise false.
	 */
	public Boolean addInstructor(String instructorName, String username) {
		return false;
	}
	
	/**
	 * Remove instructor from the course. 
	 * 
	 * @param username
	 * @return true if the instructor is removed. otherwise false.
	 */
	public Boolean removeInstructor(String username) {
		return false;
	}
	
	/**
	 * Check if the instructor is in course. 
	 * 
	 * @param username
	 * @return true if instructor is in course. Otherwise false.
	 */
	public Boolean InstructorInCourse(String username) {
		return false;
	}
	
}
