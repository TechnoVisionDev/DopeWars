package dopewars.items;

/**
 * Represents materials that can be obtained and traded.
 *
 * @author TechnoVision
 */
public enum Materials {

    ERGOT("ergot", ":ear_of_rice:", 15),
    OPIUM("opium", ":blossom:", 12),
    SASSAFRAS("sassafras", ":herb:", 28),
    COCA("coca", ":olive:", 50),
    PAPER_TAB("paper tab", ":black_square_button:", 5),
    GASOLINE("gasoline", ":fuelpump:", 45),
    ACETONE("acetone", ":canned_food:", 30);

    public final String name;
    public final String emoji;
    public final long price;

    Materials(String name, String emoji, long price) {
        this.name = name;
        this.emoji = emoji;
        this.price = price;
    }
}
