package fall2018.cscc01.team5.searchEngineWebApp.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import fall2018.cscc01.team5.searchEngineWebApp.util.Constants;

public class User {

    private String email;
    private String name;
    private String username;
    private String hash;
    private List<String> courses;
    private int permission;
    private String desc;
    private boolean emailVerified;
    private List<String> followers;

    public User(String username, String email, String name, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.username = username;
        this.email = email;
        this.name = name;
        this.hash = Validator.getSaltedHash(password, Validator.getSalt());
        this.permission = Constants.PERMISSION_ALL;
        this.courses = new ArrayList<String>();
        this.desc = "";
        emailVerified = false;
        followers = new ArrayList<>();
    }

    /**
     * Return this users list of followers
     * @return a list of user Ids
     */
    public List<String> getFollowers() {
        return followers;
    }

    /**
     * Change this users list of followers
     * @param followers the new list of followers
     */
    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    /**
     * Add a user to the follower list
     * @param id a userID of a user
     * @return true if successful
     */
    public boolean addFollower(String id) {
        if (this.followers.contains(id)) return true;
        return this.followers.add(id);
    }

    /**
     * Remove a user  from this users follower list
     * @param id the userId of a user
     * @return true if successful
     */
    public boolean removeFollower(String id) {
        return this.followers.remove(id);
    }

    /**
     * Check if this user's email is verified
     * @return true if this user's email is verified, false otherwise
     */
    public boolean isEmailVerified() {
        return emailVerified;
    }

    /**
     * Change the email verified status of this user
     * @param emailVerified the new status email verified status of this user
     */
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    /**
     * Get users name
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Get email
     *
     * @return email
     */
    public String getEmail() {
        return this.email;
    }


    /**
     * get Name
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }


    /**
     * get secure password/hash
     *
     * @return hash
     */
    public String getHash() {
        return this.hash;
    }

    /**
     * Return the permission attached to this User.
     *
     * @return a permission attached to this User
     */
    public int getPermission() {
        return this.permission;
    }

    /**
     * Return this users description
     *
     * @return this users description
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Replace this Users permission.
     *
     * @param perm - the replacement permission
     */
    public void setPermissions(int perm) {
        this.permission = perm;
    }

    /**
     * Enroll this user in a given Course.
     *
     * @param course a Course to enroll the user in
     */
    public void enrollInCourse(String course) {
        if (!isEnrolledIn(course)) this.courses.add(course);
    }

    /**
     * Enroll this user in a given Course.
     *
     * @param course a Course to enroll the user in
     */
    public void dropCourse(String course) {
        if (isEnrolledIn(course)) this.courses.remove(course);
    }

    /**
     * Check if this user is enrooled in a given user
     *
     * @param course the Course to check if this user is enrolled in
     * @return true if this user is enrolled in the given Course
     */
    public boolean isEnrolledIn(String course) {
        return this.courses.contains(course);
    }

    /**
     * Return a list of courses this user is enrolled in
     * @return a list of courses this user is enrolled in
     */
    public List<String> getCourses() {
        return this.courses;
    }

    /**
     * Change this users list of courses with a given list
     * @param courses a replacement list of courses for this user
     */
    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    /**
     * Change this users description
     *
     * @param desc the new description
     */
    public void setDescription(String desc) {
        this.desc = desc;
    }

    /**
     * Romove a given Course from this users enrolled coursed
     *
     * @param course remove this Course from the users enrolled Course
     * @return true if removing is successful
     */
    public boolean removeCourse(String course) {
        return this.courses.remove(course);
    }

    /**
     * Reset this users salted hash
     * @param hash the hash to replace this users hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Return a string representation of this user.
     *
     * @return a string representation of this user
     */
    @Override
    public String toString() {
        String result = "User: [Username: " + username
                + ", email: " + email
                + ", name: " + name
                + ", hashPassword: " + hash
                + ", courses: " + courses.toString() +
                ", Description: " + desc + "]";
        return result;
    }
}
