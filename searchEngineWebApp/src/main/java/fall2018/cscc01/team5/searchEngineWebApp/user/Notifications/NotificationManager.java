package fall2018.cscc01.team5.searchEngineWebApp.user.Notifications;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://user01:CWu73Dl13bTLZ5uD@search-engine-oslo6.mongodb.net/");

    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("search-engine");
    private static MongoCollection<Document> notiCollection = database.getCollection("notifications");

    /**
     * Add a notification to the database
     * @param notification the notification to add
     */
    public static void addNotification(Notification notification) {
        DateTime dt = new DateTime();
        Document doc = new Document("user", notification.getUser())
                .append("msg", notification.getMsg())
                .append("id", notification.getId())
                .append("hasOpened", notification.isOpened())
                .append("link", notification.getLink())
                .append("date", dt.toDateTimeISO().toString());

        notiCollection.insertOne(doc);
    }

    /**
     * Get all notifications of a user
     * @param user the user to get notification for
     * @return all notifications of a user
     */
    public static List<Notification> getNotifications(String user) {

        List<Notification> res = new ArrayList<>();

        for (Document doc: notiCollection.find(Filters.eq("user", user)).sort(new Document("date", -1))) {
            Notification noti = new Notification(doc.getString("user"), doc.getString("msg"), doc.getString("link"));
            noti.setHasOpened(doc.getBoolean("hasOpened"));
            noti.setId(doc.getString("id"));
            res.add(noti);
        }

        return res;
    }

    /**
     * Check if a user has any new notifications
     * @param user
     * @return
     */
    public static boolean hasNew(String user) {
        boolean res = false;

        for (Document doc: notiCollection.find(Filters.eq("user", user)).sort(new Document("date", -1))) {
            if (!doc.getBoolean("hasOpened")) {
                res = true;
                break;
            }
        }

        return res;
    }

    /**
     * Remove a notification by id
     * @param id the id to remove by
     */
    public static void removeNotification(String id) {
        notiCollection.deleteOne(new Document("id", id));
    }

    /**
     * Open a notification by id
     * @param id the id to open by
     */
    public static void openNotification(String id) {
        Document doc = notiCollection.find(Filters.eq("id", id)).first();
        if (doc == null) return;

        Notification noti = new Notification(doc.getString("user"), doc.getString("msg"), doc.getString("link"));
        noti.setHasOpened(true);
        noti.setId(doc.getString("id"));
        updateNotification(id, noti);
    }

    /**
     * Update an existing notification
     *
     * @param  id The id of the notification
     * @param notification the notification to update
     */
    public  static void updateNotification(String id, Notification notification) {
        Document doc = new Document("user", notification.getUser())
                .append("msg", notification.getMsg())
                .append("id", notification.getId())
                .append("hasOpened", notification.isOpened())
                .append("link", notification.getLink());

        notiCollection.updateOne(Filters.eq("id", id), new Document("$set", doc));
    }
}
