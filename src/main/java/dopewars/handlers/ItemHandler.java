package dopewars.handlers;

import dopewars.DopeWars;
import dopewars.data.cache.Player;
import dopewars.data.items.Drug;
import dopewars.data.items.Equipment;
import dopewars.data.items.Item;
import dopewars.data.items.Material;
import org.bson.Document;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages items and player inventories.
 *
 * @author TechnoVision
 */
public class ItemHandler {

    private final DopeWars bot;
    private final Map<String, Item> items;
    private final Map<String, Drug> drugs;
    private final Map<String, Material> materials;
    private final Map<String, Equipment> equipment;

    /**
     * Create and store all items.
     *
     * @param bot instance of the DopeWars bot.
     */
    public ItemHandler(DopeWars bot) {
        this.bot = bot;
        this.items = new HashMap<>();
        this.drugs = new HashMap<>();
        this.materials = new HashMap<>();
        this.equipment = new HashMap<>();

        // Create drug items
        drugs.put("cannabis", new Drug("cannabis", ":four_leaf_clover:", 30, "A drug that can be consumed for a stat buff", true));
        drugs.put("mushrooms", new Drug("mushrooms", ":mushroom:", 85, "A drug that can be consumed for a stat buff", true));
        drugs.put("peyote", new Drug("peyote", ":cactus:", 90, "A drug that can be consumed for a stat buff", true));
        drugs.put("acid", new Drug("acid", ":sparkler:", 100, "A drug that can be consumed for a stat buff"));
        drugs.put("mdma", new Drug("mdma", ":candy:", 120, "A drug that can be consumed for a stat buff"));
        drugs.put("cocaine", new Drug("cocaine", ":snowflake:", 150, "A drug that can be consumed for a stat buff"));
        drugs.put("meth", new Drug("meth", ":diamond_shape_with_a_dot_inside:", 260, "A drug that can be consumed for a stat buff"));
        drugs.put("heroin", new Drug("heroin", ":syringe:", 400, "A drug that can be consumed for a stat buff"));
        drugs.put("oxycodone", new Drug("oxycodone", ":m:", 480, "A drug that can be consumed for a stat buff"));
        drugs.put("xanax", new Drug("xanax", ":chocolate_bar:", 480, "A drug that can be consumed for a stat buff"));
        drugs.put("adderall", new Drug("adderall", ":pill:", 480, "A drug that can be consumed for a stat buff"));
        drugs.put("ketamine", new Drug("ketamine", ":unicorn:", 200, "A drug that can be consumed for a stat buff"));

        // Create material items
        materials.put("ergot", new Material("ergot", ":ear_of_rice:", 15, true));
        materials.put("opium", new Material("opium", ":blossom:", 12, true));
        materials.put("sassafras", new Material("sassafras", ":herb:", 28, true));
        materials.put("coca", new Material("coca", ":olive:", 50, true));
        materials.put("paper", new Material("paper", ":black_square_button:", 5));
        materials.put("gasoline", new Material("gasoline", ":fuelpump:", 45));
        materials.put("acetone", new Material("acetone", ":canned_food:", 30));

        // Create equipment items

        // Add to items list
        items.putAll(drugs);
        items.putAll(materials);
        items.putAll(equipment);
    }

    /**
     * Adds a specified amount of items to the user cache and database.
     *
     * @param player the player to remove the item from.
     * @param itemName the unique name of the item to add.
     * @param amount the amount of the item to add.
     */
    public void addItem(Player player, String itemName, long amount) {
        // Add to player inventory cache
        player.getInventory().merge(itemName, amount, Long::sum);

        // Add to database
        Document query = new Document("_id", player.getId());
        Document update = new Document("$inc", new Document("inventory."+itemName, amount));
        bot.databaseManager.players.updateOne(query, update);
    }

    /**
     * Remove a specified amount of items from the user cache and database.
     *
     * @param player the player to remove the item from.
     * @param itemName the unique name of the item to remove.
     * @param amount the amount of the item to remove.
     */
    public void removeItem(Player player, String itemName, long amount) {
        addItem(player, itemName, -1 * amount);
    }

    /**
     * Count the number of a specified item in a player's inventory.
     *
     * @param player the player whose inventory to search.
     * @param itemName the name of the item to count.
     * @return the number of an item in a player's inventory.
     */
    public long countItem(Player player, String itemName) {
        Long count = player.getInventory().get(itemName);
        return (count != null) ? count : 0;
    }

    /**
     * Counts the total number of items in a player's inventory.
     *
     * @param player the player to check.
     * @return the number of item's in player inventory.
     */
    public int getInventoryCount(Player player) {
        int sum = 0;
        for (Long amt : player.getInventory().values()) {
            sum += amt;
        }
        return sum;
    }

    /**
     * Checks if a player has sufficient space to add a
     * specified amount of items to their inventory.
     *
     * @param player the player to check.
     * @param amount the amount of items being added.
     * @return true if space, otherwise false.
     */
    public boolean hasInventorySpace(Player player, int amount) {
        int inventoryCount = getInventoryCount(player);
        return inventoryCount + amount <= player.getStorage();
    }

    /**
     * Retrieve an item by name.
     *
     * @param key the name of the item.
     * @return item object.
     */
    public Item getItem(String key) {
        return items.get(key);
    }

    /**
     * Retrieve a drug by name.
     *
     * @param key the name of the drug.
     * @return drug object.
     */
    public Drug getDrug(String key) {
        return drugs.get(key);
    }

    /**
     * Retrieve a material by name.
     *
     * @param key the name of the material.
     * @return material object.
     */
    public Material getMaterial(String key) {
        return materials.get(key);
    }

    /**
     * Retrieve equipment by name.
     *
     * @param key the name of the equipment.
     * @return equipment object.
     */
    public Equipment getEquipment(String key) {
        return equipment.get(key);
    }

    /**
     * Checks if an item exists by name.
     *
     * @param key the key to check.
     * @return true if item exists, otherwise false.
     */
    public boolean isItem(String key) {
        return items.containsKey(key);
    }

    public Collection<Item> getPlants() {
        return items.values().stream().filter(Item::isPlant).toList();
    }

    public Collection<Item> getItems() {
        return items.values();
    }

    public Collection<Drug> getDrugs() {
        return drugs.values();
    }

    public Collection<Material> getMaterials() {
        return materials.values();
    }

    public Collection<Equipment> getEquipment() {
        return equipment.values();
    }
}
