package dopewars.data.cache;

import dopewars.data.items.Drug;
import dopewars.data.items.Material;
import dopewars.handlers.ItemHandler;

import java.util.LinkedHashMap;

public class Market {

    private String city;
    private LinkedHashMap<String, Long> prices;

    public Market() { }

    public Market(String city, LinkedHashMap<String, Long> prices) {
        this.city = city;
        this.prices = prices;
    }

    public Market(String city, ItemHandler itemHandler) {
        this.city = city;
        this.prices = new LinkedHashMap<>();
        for (Drug drug : itemHandler.getDrugs()) {
            prices.put(drug.getName(), drug.getPrice());
        }
        for (Material mat : itemHandler.getMaterials()) {
            prices.put(mat.getName(), mat.getPrice());
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LinkedHashMap<String, Long> getPrices() {
        return prices;
    }

    public void setPrices(LinkedHashMap<String, Long> prices) {
        this.prices = prices;
    }

    public void setPrice(String itemName, long price) {
        prices.put(itemName, price);
    }
}
