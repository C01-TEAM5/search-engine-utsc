package fall2018.cscc01.team5.searchEngineWebApp.course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fall2018.cscc01.team5.searchEngineWebApp.user.User;


public class Course {

    private String name;
    private String code;
    private int size;
    private String description;
    private List<String> students;
    private List<String> files;
    private List<String> instructors;
    private List<String> teachingAssistants;

    public Course(String courseName, String courseCode, int courseSize, String[] instructors) {
        this.name = courseName;
        this.code = courseCode;
        this.size = courseSize;
        this.instructors = Arrays.asList(instructors);
        this.description = "";
        this.files = new ArrayList<>();
        this.students = new ArrayList<>();
        this.teachingAssistants = new ArrayList<>();
    }

    /**
     * Return the code of this course, the code of a course is unique
     *
     * @return the code of this course
     */
    public String getCode() {
        return code;
    }

    /**
     * Get all students in the Course.
     *
     * @return an list contains all students.
     */
    public List<String> getAllStudents() {
        return students;
    }

    /**
     * Get all TAs in thr Course.
     *
     * @return an list contains all TAs.
     */
    public List<String> getAllTAs() {
        return teachingAssistants;
    }


    /**
     * Get all Filess in thr Course.
     *
     * @return an list contains all Files.
     */
    public List<String> getAllFiles() {
        return files;
    }

    /**
     * Get all instructors in the Course.
     *
     * @return an list contains all instructors.
     */
    public List<String> getAllInstructors() {
        return instructors;
    }

    /**
     * Add a student in the Course.
     *
     * @param username
     * @return True if the student is added or student is already in Course record. otherwise false.
     */
    public boolean addStudent(String username) {
        if (this.students.contains(username)) return true;
        return this.students.add(username);
    }

    /**
     * Remove a student in the Course.
     *
     * @param username since student username is unique, we only need student username to remove student.
     * @return true if remove success. otherwise false.
     */
    public boolean removeStudent(String username) {
        return this.students.remove(username);
    }

    /**
     * Check if the student is in the Course.
     *
     * @param username
     * @return true if student in the Course. otherwise false.
     */
    public boolean studentInCourse(String username) {
        return this.students.contains(username);
    }

    /**
     * Add a TA in the Course.
     *
     * @param username
     * @return return true if TA is added to Course, or TA is already in Course. Otherwise false.
     */
    public boolean addTA(String username) {
        if (this.teachingAssistants.contains(username)) return true;
        return this.teachingAssistants.add(username);
    }

    /**
     * Remove a TA from the Course.
     *
     * @param username
     * @return true if the TA is removed. Otherwise false.
     */
    public boolean removeTA(String username) {
        return this.teachingAssistants.remove(username);
    }

    /**
     * Check if TA is in the Course.
     *
     * @param username
     * @return True if TA is in the Course. Otherwise false.
     */
    public boolean taInCourse(String username) {
        return this.teachingAssistants.contains(username);
    }

    /**
     * Add an instructor in the Course.
     *
     * @param username
     * @return true if the instructor is added, or instructor is already in Course list. otherwise false.
     */
    public boolean addInstructor(String username) {
        if (this.instructors.contains(username)) return true;
        return this.instructors.add(username);
    }

    /**
     * Remove instructor from the Course.
     *
     * @param username
     * @return true if the instructor is removed. otherwise false.
     */
    public Boolean removeInstructor(String username) {
        return this.instructors.remove(username);
    }

    /**
     * Check if the instructor is in Course.
     *
     * @param username
     * @return true if instructor is in Course. Otherwise false.
     */
    public boolean instructorInCourse(String username) {
        return this.instructors.contains(username);
    }

    /**
     * Add an instructor in the Course.
     *
     * @param id
     * @return true if the file is added, or file is already in Course list. otherwise false.
     */
    public boolean addFile(String id) {
        if (this.files.contains(id)) return false;
        return this.files.add(id);
    }

    /**
     * Remove instructor from the Course.
     *
     * @param id
     * @return true if the file is removed. otherwise false.
     */
    public Boolean removeFile(String id) {
        return this.files.remove(id);
    }

    /**
     * Check if the instructor is in Course.
     *
     * @param id
     * @return true if file is in Course. Otherwise false.
     */
    public boolean fileInCourse(String id) {
        return this.files.contains(id);
    }

    /**
     * Return the name of this course
     *
     * @return the name of this course
     */
    public String getName() {
        return name;
    }

    /**
     * Change the name of this course
     *
     * @param name a new name of this course
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the size of this course
     *
     * @return the size of this course
     */
    public int getSize() {
        return size;
    }

    /**
     * Change the size of this course
     *
     * @param size the new size of this course
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Return the description of this course
     *
     * @return the description of this course
     */
    public String getDescription() {
        return description;
    }

    /**
     * Change the description of this course
     *
     * @param description the new description of this course
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Change the code of this course
     *
     * @param code the new code of this course
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Change the list of files of this course
     *
     * @param code the new code of this course
     */
    public void setFiles(List<String> instructors) {
        this.code = code;
    }

    /**
     * Change the list of instructors of this course
     *
     * @param instructors the new instructors of this course
     */
    public void setInstructors(List<String> instructors) {
        this.instructors = instructors;
    }

    /**
     * Change the code of this course
     *
     * @param code the new code of this course
     */
    public void setStudents(List<String> instructors) {
        this.code = code;
    }

    /**
     * Change the code of this course
     *
     * @param code the new code of this course
     */
    public void setTAs(List<String> instructors) {
        this.code = code;
    }
}
