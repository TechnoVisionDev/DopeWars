package dopewars.data.items;

/**
 * An item that can be acquired by a player.
 *
 * @author TechnoVision
 */
public abstract class Item {

    private final String name;
    private final String emoji;
    private final long price;
    private final String description;
    private final boolean isPlant;

    public Item(String name, String emoji, long price, String description, boolean isPlant) {
        this.name = name;
        this.emoji = emoji;
        this.price = price;
        this.description = description;
        this.isPlant = isPlant;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPlant() {
        return isPlant;
    }
}
