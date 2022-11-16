package dopewars.handlers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dopewars.data.DatabaseManager;
import dopewars.data.pojos.Player;
import dopewars.items.ItemTypes;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles interaction with the in-game economy.
 *
 * @author TechnoVision
 */
public class EconomyHandler {

    private final DatabaseManager databaseManager;

    /**
     * Caches all data from database on startup
     *
     * @param databaseManager an instance of DatabaseManager.java from DopeWars.java
     */
    public EconomyHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Deposit cash into the player's bank account.
     *
     * @param player the player depositing.
     * @param amount the amount to deposit.
     */
    public void deposit(Player player, long amount) {
        // Update player cache
        player.setCash(player.getCash() - amount);
        player.setBank(player.getBank() + amount);

        // Update database
        Document query = new Document("user_id", player.getUser_id());
        Bson update = Updates.inc("cash", -1 * amount);
        Bson update2 = Updates.inc("bank", amount);
        databaseManager.players.updateOne(query, Filters.and(update, update2));
    }

    /**
     * Withdraw cash from the player's bank account.
     *
     * @param player the player withdrawing.
     * @param amount the amount to withdraw.
     */
    public void withdraw(Player player, long amount) {
        // Update player cache
        player.setBank(player.getBank() - amount);
        player.setCash(player.getCash() + amount);

        // Update database
        Document query = new Document("user_id", player.getUser_id());
        Bson update = Updates.inc("cash", amount);
        Bson update2 = Updates.inc("bank", -1 * amount);
        databaseManager.players.updateOne(query, Filters.and(update, update2));
    }
}
