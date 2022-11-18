package dopewars.handlers;

import com.mongodb.client.model.Filters;
import dopewars.DopeWars;
import dopewars.data.cache.Market;
import dopewars.data.cache.Player;
import dopewars.data.items.Drug;
import dopewars.data.items.Item;
import dopewars.data.items.Material;
import dopewars.util.enums.Cities;
import dopewars.util.enums.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.*;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.enums.Emojis.CURRENCY;

/**
 * Handles interactions with various markets and cities
 *
 * @author TechnoVision
 */
public class MarketHandler {

    private final DopeWars bot;
    private final Map<String, Market> markets;
    private final Map<String, LinkedHashMap<String, Listing>> listings;

    /**
     * Setup and cache market prices and listings.
     *
     * @param bot instance of DopeWars bot.
     */
    public MarketHandler(DopeWars bot) {
        this.bot = bot;
        this.markets = new HashMap<>();
        this.listings = new LinkedHashMap<>();

        // Setup markets
        for (Cities city : Cities.values()) {
            Market market = bot.databaseManager.markets.find(Filters.eq("city", city.toString())).first();
            if (market == null) {
                //Create new market for this city
                market = new Market(city.toString(), bot.itemHandler);
                bot.databaseManager.markets.insertOne(market);
            }
            // Cache market
            markets.put(market.getCity(), market);
            updateListings(market);
        }
    }

    /**
     * Update market by selecting 12 random listings.
     *
     * @param market the market to update.
     */
    private void updateListings(Market market) {
        // TODO: Update prices based on supply & demand

        // Generate new listings for market
        LinkedHashMap<String, Listing> cityListings = new LinkedHashMap<>();
        ArrayList<Drug> drugs = new ArrayList<>(bot.itemHandler.getDrugs());
        Collections.shuffle(drugs);
        for (int i=0; i < 6; i++) {
            Drug drug = drugs.get(i);
            cityListings.put(drug.getName(), new Listing(drug, market.getPrices().get(drug.getName())));
        }
        ArrayList<Material> materials = new ArrayList<>(bot.itemHandler.getMaterials());
        Collections.shuffle(materials);
        for (int i=0; i < 6; i++) {
            Material material = materials.get(i);
            cityListings.put(material.getName(), new Listing(material, market.getPrices().get(material.getName())));
        }
        listings.put(market.getCity(), cityListings);
    }

    /**
     * Police bust player for selling drugs, removes all sold items.
     *
     * @param player the player getting busted.
     * @param username the username of the player.
     * @param item the item being sold.
     * @param quantity the amount of item being sold.
     * @return a message embed specifying details of the bust.
     */
    public MessageEmbed bustedSelling(Player player, String username, Item item, long quantity) {
        bot.itemHandler.removeItem(player, item.getName(), quantity);
        return new EmbedBuilder()
                .setColor(EmbedColor.ERROR.color)
                .setTitle(":police_officer: BUSTED!")
                .setDescription("**"+username+"** was caught selling "+quantity+" "+item.getEmoji()+" "+item.getName()+" and had it all confiscated!")
                .build();
    }

    /**
     * Police bust player for buying drugs, removes all cash spent.
     *
     * @param player the player getting busted.
     * @param username the username of the player.
     * @param cash the cash spent on drugs.
     * @return a message embed specifying details of the bust.
     */
    public MessageEmbed bustedBuying(Player player, String username, long cash) {
        bot.economyHandler.removeMoney(player, cash);
        return new EmbedBuilder()
                .setColor(EmbedColor.ERROR.color)
                .setTitle(":police_officer: BUSTED!")
                .setDescription("**"+username+"** was caught buying "+NUM_FORMAT.format(cash)+" "+CURRENCY+" worth of drugs and had it all confiscated!")
                .build();
    }

    /**
     * Check if an item is listed in a given city's market
     *
     * @param city the city to check.
     * @param itemName the name of the item to check.
     * @return true if city is listing item, otherwise false.
     */
    public boolean hasItemListed(String city, String itemName) {
        return bot.marketHandler.getListings(city).containsKey(itemName);
    }

    /**
     * Retrieve all the listings for a specified city's market.
     *
     * @param city the city whose listings we are retrieving.
     * @return a map of item names to listing objects.
     */
    public Map<String, Listing> getListings(String city) {
        return listings.get(city);
    }

    /**
     * Retrieve a single listing given an item name
     *
     * @param city the city from which we are retrieving the listing.
     * @param itemName the name of the listed item being retrieved.
     * @return a listing object or null if listing doesn't exist.
     */
    public Listing getListing(String city, String itemName) {
        return listings.get(city).get(itemName);
    }

    /**
     * Represents an item available to buy and sell on a market.
     *
     * @param item the item listed.
     * @param price the price of the listed item.
     */
    public record Listing(Item item, Long price) { }
}
