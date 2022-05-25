package civbot.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches data from MongoDB in local memory for quick access.
 *
 * @author TechnoVision
 */
public class Cache {

    private final Map<Long, User> users;

    /**
     * Caches all data from database on startup
     *
     * @param databaseManager an instance of DatabaseManager.java from CivBot.java
     */
    public Cache(DatabaseManager databaseManager) {
        users = new HashMap<>();
        for (User user : databaseManager.users.find()) {
            users.put(user.getUser_id(), user);
        }
    }

    public void addUser(User user) {
        users.put(user.getUser_id(), user);
    }

    public User getUser(long id) {
        return users.get(id);
    }
}
