package dopewars.handlers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dopewars.DopeWars;
import dopewars.data.DatabaseManager;
import dopewars.data.cache.Player;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

/**
 * Caches player data from MongoDB in local memory for quick access.
 * Some methods also update data in the database.
 *
 * @author TechnoVision
 */
public class PlayerHandler {

    private final DopeWars bot;
    private final Map<Long, Player> players;

    /**
     * Caches player data on startup.
     */
    public PlayerHandler(DopeWars bot) {
        this.bot = bot;
        this.players = new HashMap<>();
        for (Player player : bot.databaseManager.players.find()) {
            addPlayer(player);
        }
    }

    /**
     * Add a player object to the cache.
     *
     * @param player database data for this player.
     */
    public void addPlayer(Player player) {
        players.put(player.getId(), player);
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
     * Changes the city that a player is in.
     *
     * @param player the player changing locations.
     * @param city the city the player is moving to.
     */
    public void fly(Player player, String city) {
        player.setCity(city);
        Bson filter = Filters.eq("_id", player.getId());
        Bson update = Updates.set("city", city);
        bot.databaseManager.players.updateOne(filter, update);
    }
}
