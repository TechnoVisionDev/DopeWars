package dopewars.handlers;

import com.mongodb.client.model.Filters;
import dopewars.DopeWars;
import dopewars.data.cache.Market;
import dopewars.data.cache.Player;
import dopewars.util.Cities;

import java.util.*;

/**
 * Handles interactions with various markets and cities
 *
 * @author TechnoVision
 */
public class MarketHandler {

    private final DopeWars bot;
    private final Map<String, Market> markets;
    private final Map<String, Map<String, Long>> currentDrugs;
    private final Map<String, Map<String, Long>> currentMaterials;

    public MarketHandler(DopeWars bot) {
        this.bot = bot;
        this.markets = new HashMap<>();
        this.currentDrugs = new HashMap<>();
        this.currentMaterials = new HashMap<>();

        // Setup markets
        for (Cities city : Cities.values()) {
            Market market = bot.databaseManager.markets.find(Filters.eq("city", city.toString())).first();
            if (market == null) {
                //Create new market for this city
                market = new Market(city.toString());
                bot.databaseManager.markets.insertOne(market);
            }
            // Cache market
            markets.put(market.getCity(), market);
            updateMarket(market);
        }
    }

    private void updateMarket(Market market) {
        // Generate list of 6 random drugs
        List<String> keys = new ArrayList<>(market.getDrugs().keySet());
        Collections.shuffle(keys);
        Map<String, Long> drugMap = new HashMap<>();
        for (int i=0; i < 6; i++) {
            String key = keys.get(i);
            drugMap.put(key, market.getDrugs().get(key));
        }
        currentDrugs.put(market.getCity(), drugMap);

        // Generate list of 6 random materials
        keys = new ArrayList<>(market.getMaterials().keySet());
        Collections.shuffle(keys);
        Map<String, Long> matMap = new HashMap<>();
        for (int i=0; i < 6; i++) {
            String key = keys.get(i);
            matMap.put(key, market.getDrugs().get(key));
        }
        currentMaterials.put(market.getCity(), matMap);
    }

    public boolean hasDrug(String city, String drugName) {
        return bot.marketHandler.getCurrentDrugs(city).containsKey(drugName);
    }

    public boolean hasMaterial(String city, String matName) {
        return bot.marketHandler.getCurrentMaterials(city).containsKey(matName);
    }

    public Long getDrugPrice(String city, String drugName) {
        return bot.marketHandler.getCurrentDrugs(city).get(drugName);
    }

    public Long getMaterialPrice(String city, String matName) {
        return bot.marketHandler.getCurrentMaterials(city).get(matName);
    }

    public Map<String, Long> getCurrentDrugs(String city) {
        return currentDrugs.get(city);
    }

    public Map<String, Long> getCurrentMaterials(String city) {
        return currentMaterials.get(city);
    }
}
