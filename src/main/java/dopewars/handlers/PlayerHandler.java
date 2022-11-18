package dopewars.handlers;

import dopewars.data.DatabaseManager;
import dopewars.data.cache.Player;
import dopewars.items.ItemTypes;
import org.bson.Document;

import java.util.*;

/**
 * Caches player data from MongoDB in local memory for quick access.
 * Some methods also update data in the database.
 *
 * @author TechnoVision
 */
public class PlayerHandler {

    private final DatabaseManager databaseManager;
    private final Map<Long, Player> players;

    /**
     * Caches all data from database on startup
     *
     * @param databaseManager an instance of DatabaseManager.java from DopeWars.java
     */
    public PlayerHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.players = new HashMap<>();
        for (Player player : databaseManager.players.find()) {
            addPlayer(player);
        }
    }

    /**
     * Add a player object to the cache.
     *
     * @param player database data for this player.
     */
    public void addPlayer(Player player) {
        players.put(player.getUser_id(), player);
    }

    /**
     * Retrieves a player object by discord user ID.
     *
     * @param id the discord user ID for this player.
     * @return player POJO object.
     */
    public Player getPlayer(long id) {
        return players.get(id);
    }

    /**
     * Adds a specified amount of items to the user cache and database.
     *
     * @param user_id the discord user ID for this player.
     * @param key the unique key of the item to add.
     * @param amount the amount of the item to add.
     */
    public void addItem(long user_id, String key, long amount, ItemTypes type) {
        // Add to player cache
        switch(type) {
            case MATERIALS -> players.get(user_id).getMaterials().merge(key, amount, Long::sum);
            case EQUIPMENT -> players.get(user_id).getEquipment().merge(key, amount, Long::sum);
            case DRUGS -> players.get(user_id).getDrugs().merge(key, amount, Long::sum);
        }

        // Add to database
        Document query = new Document("user_id", user_id);
        Document update = new Document("$inc", new Document(type.toString().toLowerCase()+"."+key, amount));
        databaseManager.players.updateOne(query, update);
    }

    /**
     * Remove a specified amount of items from the user cache and database.
     *
     * @param user_id the discord user ID for this player.
     * @param key the unique key of the item to remove.
     * @param amount the amount of the item to remove.
     */
    public void removeItem(long user_id, String key, long amount, ItemTypes type) {
        // Add to player cache
        switch(type) {
            case MATERIALS -> players.get(user_id).getMaterials().merge(key, -1*amount, Long::sum);
            case EQUIPMENT -> players.get(user_id).getEquipment().merge(key, -1*amount, Long::sum);
            case DRUGS -> players.get(user_id).getDrugs().merge(key, -1*amount, Long::sum);
        }

        // Add to database
        Document query = new Document("user_id", user_id);
        Document update = new Document("$inc", new Document(type.toString().toLowerCase()+"."+key, -1*amount));
        databaseManager.players.updateOne(query, update);
    }

    /**
     * Count the number of a specified item in a player's inventory.
     *
     * @param player the player whose inventory to search.
     * @param key the item name to count.
     * @return the number of an item in a player's inventory.
     */
    public long countItem(Player player, String key) {
        Long count = player.getDrugs().get(key);
        if (count != null) return count;
        count = player.getMaterials().get(key);
        if (count != null) return count;
        return 0;
    }
}
