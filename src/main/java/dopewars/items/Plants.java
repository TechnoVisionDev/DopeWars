package dopewars.items;

/**
 * Represents plants that can be grown using the /grow command.
 *
 * @author TechnoVision
 */
public enum Plants {

    CANNABIS("cannabis", ":four_leaf_clover:"),
    MUSHROOMS("mushrooms", ":mushroom:"),
    ERGOT("ergot", ":ear_of_rice:"),
    OPIUM("opium", ":blossom:"),
    SASSAFRAS("sassafras", ":herb:");

    public final String name;
    public final String emoji;

    Plants(String name, String emoji) {
        this.name = name;
        this.emoji = emoji;
    }
}
