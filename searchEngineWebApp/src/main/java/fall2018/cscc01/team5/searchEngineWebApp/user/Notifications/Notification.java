package fall2018.cscc01.team5.searchEngineWebApp.user.Notifications;

import java.util.UUID;

public class Notification {

    private String id;
    private String user;
    private String msg;
    private boolean isOpened;
    private String link;

    /**
     * Create a new notification
     *
     * @param user the user of the notification
     * @param msg the message of the notification
     * @param link the link of the notification
     */
    public Notification(String user, String msg, String link) {
        this.id = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replaceAll("-", "");
        this.user = user;
        this.msg = msg;
        this.link = link;
        this.isOpened = false;
    }

    /**
     * Get the id of this notification
     * @return the id of this notification
     */
    public String getId() {
        return id;
    }

    /**
     * Change the id of this notification
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the user of this notification
     * @return the user of this notification
     */
    public String getUser() {
        return user;
    }

    /**
     * Change the user fo this notification
     * @param user the new user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Return the message of this notification
     * @return the message of this notification
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Change the message of this notification
     * @param msg the new message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Check if this notification is opened
     * @return true if notification in opened, false otherwise
     */
    public boolean isOpened() {
        return isOpened;
    }

    /**
     * Change the opened status of this notification
     * @param hasOpened the new status
     */
    public void setHasOpened(boolean hasOpened) {
        this.isOpened = hasOpened;
    }

    /**
     * Get the link of this notification
     * @return the link of this notification
     */
    public String getLink() {
        return link;
    }

    /**
     * Change the link of this notification
     * @param link the new link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Return a string representation of this notification
     * @return a string representation of this notification
     */
    public String toString() {
        return "Notification: [ User: " + user + ", " +
                " Message: " + msg + ", " +
                " Opened: " + isOpened + ", " +
                " Link: " + link + " ]";
    }
}
