package fall2018.cscc01.team5.searchEngineWebApp.document.crawler;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import fall2018.cscc01.team5.searchEngineWebApp.user.AccountManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.User;
import fall2018.cscc01.team5.searchEngineWebApp.user.Notifications.Notification;
import fall2018.cscc01.team5.searchEngineWebApp.user.Notifications.NotificationManager;
import fall2018.cscc01.team5.searchEngineWebApp.user.login.InvalidUsernameException;

public class CrawlRunner implements Runnable {

    private String crawlSite;
    private String currentUser;
    private String courseId;
    
    public CrawlRunner(String crawlSite, String currentUser, String courseId) {
        this.crawlSite = crawlSite;
        this.currentUser = currentUser;
        this.courseId = courseId;
    }
    
    @Override
    public void run() {
        //String msg = "You have started a crawl on " + crawlSite + ". You will be notified when it has completed.";
        //NotificationManager.addNotification(new Notification(currentUser, msg, "/profile?id=" + currentUser));
        
        //Crawl the website
        try {
            CrawlerController controller = new CrawlerController(crawlSite, 2, 40, currentUser, courseId);
            controller.startCrawl();
        } catch (Exception e) {
            String failmsg = "Crawl on + " + crawlSite + "has completed but there was a problem in the upload.";
            NotificationManager.addNotification(new Notification(currentUser, failmsg, "/profile?id=" + currentUser));
            return;
        }        
        
        String msg2 = "Crawl on "+ crawlSite + " has completed succesfully.";
        NotificationManager.addNotification(new Notification(currentUser, msg2, "/profile?id=" + currentUser));
        
        User user;
        try {
            user = AccountManager.getUser(currentUser);
            for (String id: user.getFollowers()) {
                String followmsg = "Someone that you follow has just finished a crawl on "+crawlSite+".";
                NotificationManager.addNotification(new Notification(id, followmsg, "/profile?id=" + currentUser));
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidUsernameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
    }
    
}
