package dopewars.data.pojos;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private long user_id;
    private long cash;
    private long bank;
    private int attack;
    private int defense;
    private int maxHealth;
    private int health;
    private int level;
    private int prestige;
    private Map<String, Long> materials;
    private Map<String, Long> equipment;
    private Map<String, Long> drugs;

    public Player() { }

    public Player(long user_id, long cash, long bank, int attack, int defense, int maxHealth, int health, int level, int prestige, Map<String, Long> materials, Map<String, Long> equipment, Map<String, Long> drugs) {
        this.user_id = user_id;
        this.cash = cash;
        this.bank = bank;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = health;
        this.level = level;
        this.prestige = prestige;
        this.materials = materials;
        this.equipment = equipment;
        this.drugs = drugs;
    }

    public Player(long user_id) {
        this.user_id = user_id;
        this.cash = 0;
        this.bank = 0;
        this.attack = 1;
        this.defense = 1;
        this.maxHealth = 100;
        this.health = 100;
        this.level = 1;
        this.prestige = 0;
        this.materials = new HashMap<>();
        this.equipment = new HashMap<>();
        this.drugs = new HashMap<>();
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public Map<String, Long> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<String, Long> materials) {
        this.materials = materials;
    }

    public Map<String, Long> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<String, Long> equipment) {
        this.equipment = equipment;
    }

    public Map<String, Long> getDrugs() {
        return drugs;
    }

    public void setDrugs(Map<String, Long> drugs) {
        this.drugs = drugs;
    }
}
