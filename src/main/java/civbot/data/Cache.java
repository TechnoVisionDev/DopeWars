package civbot.data;

import civbot.data.pojos.Player;
import org.bson.Document;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Caches data from MongoDB in local memory for quick access.
 * Some methods also update data in the database.
 *
 * @author TechnoVision
 */
public class Cache {

    private static final int FIVE_MINUTES = 5 * 60 * 1000;

    private final DatabaseManager databaseManager;
    private final Map<Long, Player> users;
    private final Map<Long, Long> timeouts;

    /**
     * Caches all data from database on startup
     *
     * @param databaseManager an instance of DatabaseManager.java from CivBot.java
     */
    public Cache(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.users = new HashMap<>();
        this.timeouts = new HashMap<>();
        for (Player player : databaseManager.players.find()) {
            users.put(player.getUser_id(), player);
        }
    }

    /**
     * Add a player object to the cache.
     *
     * @param player database data for this player.
     */
    public void addPlayer(Player player) {
        users.put(player.getUser_id(), player);
    }

    /**
     * Retrieves a player object by discord user ID.
     *
     * @param id the discord user ID for this player.
     * @return player POJO object.
     */
    public Player getPlayer(long id) {
        return users.get(id);
    }

    /**
     * Adds a specified amount of items to the user cache and database.
     *
     * @param user_id the discord user ID for this player.
     * @param key the unique key of the item to add.
     * @param amount the amount of the item to add.
     * @return true is successful, false if on cooldown.
     */
    public boolean addItem(long user_id, String key, long amount) {
        // Check if cooldown timer is active
        if (timeouts.containsKey(user_id)) {
            if ((timeouts.get(user_id) - System.currentTimeMillis() + FIVE_MINUTES) > 0) {
                return false;
            }
        }

        // Add to cache
        users.get(user_id).getInventory().merge(key, amount, Long::sum);
        timeouts.put(user_id, System.currentTimeMillis());

        // Add to database
        Document query = new Document("user_id", user_id);
        Document update = new Document("$inc", new Document("inventory."+key, amount));
        databaseManager.players.updateOne(query, update);
        return true;
    }

    public String getGatherCooldown(long user_id) {
        if (timeouts.containsKey(user_id)) {
            long cooldown = FIVE_MINUTES - (System.currentTimeMillis() - timeouts.get(user_id));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldown) - TimeUnit.MINUTES.toSeconds(minutes);
            return minutes + "m " + seconds + "s";
        }
        return "0m 0s";
    }
}
