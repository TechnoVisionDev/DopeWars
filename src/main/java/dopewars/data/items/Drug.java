package dopewars.data.items;

/**
 * A consumable item that provides temporary effects.
 *
 * @author TechnoVision
 */
public class Drug extends Item {

    public Drug(String name, String emoji, long price, String description, boolean isPlant) {
        super(name, emoji, price, description, isPlant);
    }

    public Drug(String name, String emoji, long price, String description) {
        super(name, emoji, price, description, false);
    }
}
