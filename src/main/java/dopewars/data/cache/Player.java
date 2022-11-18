package dopewars.data.cache;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private long id;
    private long cash;
    private long bank;
    private int attack;
    private int defense;
    private int maxHealth;
    private int health;
    private int storage;
    private int level;
    private int prestige;
    private String city;
    private Map<String, Long> inventory;

    public Player() { }

    public Player(long id, long cash, long bank, int attack, int defense, int maxHealth, int health, int storage, int level, int prestige, String city, Map<String, Long> inventory) {
        this.id = id;
        this.cash = cash;
        this.bank = bank;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = health;
        this.storage = storage;
        this.level = level;
        this.prestige = prestige;
        this.city = city;
        this.inventory = inventory;
    }

    public Player(long id) {
        this.id = id;
        this.cash = 0;
        this.bank = 0;
        this.attack = 1;
        this.defense = 1;
        this.maxHealth = 100;
        this.health = 100;
        this.storage = 50;
        this.level = 1;
        this.prestige = 0;
        this.city = "LOS_ANGELES";
        this.inventory = new HashMap<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCash() {
        return cash;
    }

    public void setCash(long cash) {
        this.cash = cash;
    }

    public long getBank() {
        return bank;
    }

    public void setBank(long bank) {
        this.bank = bank;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Map<String, Long> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Long> inventory) {
        this.inventory = inventory;
    }
}
