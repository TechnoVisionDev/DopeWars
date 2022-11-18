package dopewars.handlers;

import dopewars.DopeWars;
import dopewars.data.DatabaseManager;
import dopewars.data.cache.Player;

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
}
