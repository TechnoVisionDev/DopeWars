package dopewars.data.items;

/**
 * An equitable item that gives players permanent stat buffs.
 *
 * @author TechnoVision
 */
public class Equipment extends Item {

    public Equipment(String name, String emoji, long price, String description) {
        super(name, emoji, price, description, false);
    }
}
