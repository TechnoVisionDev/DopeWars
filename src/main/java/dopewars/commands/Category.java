package dopewars.commands;

/**
 * Category that represents a group of similar commands.
 * Each category has a name and an emoji.
 *
 * @author TechnoVision
 */
public enum Category {
    PLAYER(":man_bowing:", "Player"),
    ECONOMY(":moneybag:", "Economy"),
    MARKET(":scales:", "Market"),
    CASINO(":game_die:", "Casino"),
    FARMING(":farmer:", "Farming"),
    CRAFTING(":tools:", "Crafting"),
    UTILITY(":gem:", "Utility");

    public final String emoji;
    public final String name;

    Category(String emoji, String name) {
        this.emoji = emoji;
        this.name = name;
    }
}
