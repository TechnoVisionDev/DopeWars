package dopewars.items;

/**
 * Represents drugs that can be obtained, consumed, and traded.
 *
 * @author TechnoVision
 */
public enum Drugs {

    CANNABIS("cannabis", ":four_leaf_clover:", 30),
    MUSHROOMS("mushrooms", ":mushroom:", 85),
    PEYOTE("peyote", ":cactus:", 90),
    ACID("acid", ":sparkler:", 100),
    MDMA("mdma", ":candy:", 120),
    COCAINE("cocaine", ":snowflake:", 150),
    METH("meth", ":diamond_shape_with_a_dot_inside:", 260),
    HEROIN("heroin", ":syringe:", 400),
    OXYCODONE("oxycodone", ":m:", 480),
    XANAX("xanax", ":chocolate_bar:", 480),
    ADDERALL("adderall", ":pill:", 480),
    KETAMINE("ketamine", ":unicorn:", 200);

    public final String name;
    public final String emoji;
    public final long price;

    Drugs(String name, String emoji, long price) {
        this.name = name;
        this.emoji = emoji;
        this.price = price;
    }
}
