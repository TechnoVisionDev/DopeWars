package civbot.data.pojos;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private long user_id;
    private long gold;
    private Map<String, Long> inventory;

    public Player() { }

    public Player(long user_id, long gold, Map<String, Long> inventory) {
        this.user_id = user_id;
        this.gold = gold;
        this.inventory = inventory;
    }

    public Player(long user_id) {
        this.user_id = user_id;
        this.gold = 0;
        this.inventory = new HashMap<>();
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public Map<String, Long> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Long> inventory) {
        this.inventory = inventory;
    }
}
