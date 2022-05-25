package civbot.util;

/**
 * Represents each profession in the game, each with a unique name and ID.
 *
 * @author TechnoVision
 */
public enum Professions {

    WOODCUTTING("woodcutting", "Chops and refines wood from trees.", ":axe:", 0),
    HERBALISM("herbalism", "Gathers herbs and exotic plants.", ":herb:", 1),
    LEATHERWORKING("leatherworking", "Skins animal hides and stitches them into items.", ":deer:", 2),
    BLACKSMITHING("blacksmithing", "Mines ores and smiths them into items.", ":crossed_swords:", 3);

    public final String name;
    public final String description;
    public final String emoji;
    public final int id;

    Professions(String name, String description, String emoji, int id) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.id = id;
    }
}
