package dopewars.items;

/**
 * Represents materials that can be obtained and traded.
 *
 * @author TechnoVision
 */
public enum Materials {

    ERGOT("ergot", ":ear_of_rice:"),
    OPIUM("opium", ":blossom:"),
    SASSAFRAS("sassafras", ":herb:");

    public final String name;
    public final String emoji;

    Materials(String name, String emoji) {
        this.name = name;
        this.emoji = emoji;
    }
}
