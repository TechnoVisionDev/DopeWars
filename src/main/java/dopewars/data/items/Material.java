package dopewars.data.items;

/**
 * An item that is used in crafting recipes.
 *
 * @author TechnoVision
 */
public class Material extends Item {

    public Material(String name, String emoji, long price, boolean isPlant) {
        super(name, emoji, price, "A material used for crafting.", isPlant);
    }

    public Material(String name, String emoji, long price) {
        super(name, emoji, price, "A material used for crafting.", false);
    }
}
