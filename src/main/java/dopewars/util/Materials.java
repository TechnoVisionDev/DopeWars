package dopewars.util;

/**
 * Represents materials that can be obtained and traded.
 *
 * @author TechnoVision
 */
public enum Materials {

    OAK_LOG("oak log", ":wood:", 0);

    public final String name;
    public final String emoji;
    public final int id;

    Materials(String name, String emoji, int id) {
        this.name = name;
        this.emoji = emoji;
        this.id = id;
    }
}
