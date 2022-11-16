package dopewars.handlers.economy;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dopewars.DopeWars;
import dopewars.data.pojos.Player;
import dopewars.handlers.TimeoutHandler;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles interaction with the in-game economy.
 *
 * @author TechnoVision
 */
public class EconomyHandler {

    private final DopeWars bot;
    private final EconomyLocalization responses;

    /**
     * Setup economy handler.
     *
     * @param bot an instance of the DopeWars bot.
     */
    public EconomyHandler(DopeWars bot) {
        this.bot = bot;
        this.responses = new EconomyLocalization();
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
        bot.databaseManager.players.updateOne(query, Filters.and(update, update2));
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
        bot.databaseManager.players.updateOne(query, Filters.and(update, update2));
    }


    /**
     * Add money to this user's account
     *
     * @param amount the amount of money to add.
     */
    public void addMoney(Player player, long amount) {
        player.setCash(player.getCash() + amount);
        Bson query = Filters.eq("user_id", player.getUser_id());
        Bson update = Updates.inc("cash", amount);
        bot.databaseManager.players.updateOne(query, update);
    }

    /**
     * Remove money to this user's account
     *
     * @param amount the amount of money to remove.
     */
    public void removeMoney(Player player, long amount) {
        player.setCash(player.getCash() - amount);
        Bson query = Filters.eq("user_id", player.getUser_id());
        Bson update = Updates.inc("cash", -1 * amount);
        bot.databaseManager.players.updateOne(query, update);
    }

    /**
     * 40% chance to add 250-700 to user's balance.
     * 60% chance to lose 20-40% of user's balance.
     *
     * @param player the player comitting the crime.
     * @return an EconomyReply object with response, ID number, and success boolean.
     */
    public EconomyReply crime(Player player) {
        long amount;
        EconomyReply reply;
        if (ThreadLocalRandom.current().nextInt(100) <= 40) {
            // Crime successful
            amount = ThreadLocalRandom.current().nextInt(450) + 250;
            addMoney(player, amount);
            reply = responses.getCrimeSuccessResponse(amount);
        } else {
            // Crime failed
            amount = calculateFine(player);
            if (amount > 0) removeMoney(player, amount);
            reply = responses.getCrimeFailResponse(amount);
        }
        bot.timeoutHandler.addTimeout(player.getUser_id(), TimeoutHandler.TimeoutType.CRIME);
        return reply;
    }

    /**
     * Calculate fine for commands like /crime and /rob
     * Default fine is 20-40% of player's networth.
     *
     * @param player the player to calculate fine for.
     * @return the calculated fine amount.
     */
    private long calculateFine(Player player) {
        long networth = player.getCash() + player.getBank();
        long fine = 0;
        if (networth > 0) {
            double percent = (ThreadLocalRandom.current().nextInt(20) + 20) * 0.01;
            fine = (long) (networth * percent);
        }
        return fine;
    }
}
