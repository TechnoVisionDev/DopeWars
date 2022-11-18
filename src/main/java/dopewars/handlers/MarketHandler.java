package dopewars.handlers;

import com.mongodb.client.model.Filters;
import dopewars.DopeWars;
import dopewars.data.cache.Market;
import dopewars.data.items.Drug;
import dopewars.data.items.Item;
import dopewars.data.items.Material;
import dopewars.util.enums.Cities;

import java.util.*;

/**
 * Handles interactions with various markets and cities
 *
 * @author TechnoVision
 */
public class MarketHandler {

    private final DopeWars bot;
    private final Map<String, Market> markets;
    private final Map<String, LinkedHashMap<String, Listing>> listings;

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
            updateMarket(market);
        }
    }

    private void updateMarket(Market market) {
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

    public boolean hasItem(String city, String itemName) {
        return bot.marketHandler.getListings(city).containsKey(itemName);
    }

    public Map<String, Listing> getListings(String city) {
        return listings.get(city);
    }

    public Listing getListing(String city, String itemName) {
        return listings.get(city).get(itemName);
    }

    public record Listing(Item item, Long price) { }
}
