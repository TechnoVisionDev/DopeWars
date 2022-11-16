package dopewars.items;

/**
 * Represents equipment that can be crafted and equipped.
 *
 * @author TechnoVision
 */
public enum Equipment {

    KNIFE("knife", ":knife:", 0);

    public final String name;
    public final String emoji;
    public final int id;

    Equipment(String name, String emoji, int id) {
        this.name = name;
        this.emoji = emoji;
        this.id = id;
    }
}
