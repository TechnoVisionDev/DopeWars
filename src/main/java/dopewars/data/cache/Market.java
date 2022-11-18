package dopewars.data.cache;

import dopewars.items.Drugs;
import dopewars.items.Materials;

import java.util.HashMap;
import java.util.Map;

public class Market {

    private String city;
    private Map<String, Long> drugs;
    private Map<String, Long> materials;

    public Market() { }

    public Market(String city, Map<String, Long> drugs, Map<String, Long> materials) {
        this.city = city;
        this.drugs = drugs;
        this.materials = materials;
    }

    public Market(String city) {
        this.city = city;
        this.drugs = new HashMap<>();
        this.materials = new HashMap<>();
        for (Drugs drug : Drugs.values()) {
            drugs.put(drug.toString(), drug.price);
        }
        for (Materials item : Materials.values()) {
            materials.put(item.toString(), item.price);
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Map<String, Long> getDrugs() {
        return drugs;
    }

    public void setDrugs(Map<String, Long> drugs) {
        this.drugs = drugs;
    }

    public Map<String, Long> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<String, Long> materials) {
        this.materials = materials;
    }
}
