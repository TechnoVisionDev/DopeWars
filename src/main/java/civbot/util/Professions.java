package civbot.util;

public enum Professions {

    WOODCUTTING("woodcutting", 0),
    HERBALISM("herbalism", 1),
    SKINNING("skinning", 2);

    public final String name;
    public final int id;

    private Professions(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
