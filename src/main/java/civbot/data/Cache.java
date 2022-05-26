package civbot.data;

import civbot.data.pojos.Player;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches data from MongoDB in local memory for quick access.
 * Some methods also update data in the database.
 *
 * @author TechnoVision
 */
public class Cache {

    private final DatabaseManager databaseManager;
    private final Map<Long, Player> users;

    /**
     * Caches all data from database on startup
     *
     * @param databaseManager an instance of DatabaseManager.java from CivBot.java
     */
    public Cache(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        users = new HashMap<>();
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
     */
    public void addItem(long user_id, String key, long amount) {
        users.get(user_id).getInventory().merge(key, amount, Long::sum);
        Document query = new Document("user_id", user_id);
        Document update = new Document("$inc", new Document("inventory.wooden_log", amount));
        databaseManager.players.updateOne(query, update);
    }
}
