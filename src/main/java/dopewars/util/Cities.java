package dopewars.util;

public enum Cities {

    AMSTERDAM("Mexico City", "AMS", 50, ":flag_nl:"),
    BARCELONA("Barcelona", "BCN", 75, ":flag_es:"),
    BERLIN("Berlin", "BE", 45, ":flag_de:"),
    LONDON("London", "LDN", 175, ":flag_gb:"),
    LOS_ANGELES("Los Angeles", "LA", 15, ":flag_us:"),
    MEXICO_CITY("Mexico City", "CDMX", 50, ":flag_mx:"),
    MONTREAL("Montreal", "MTL", 90, ":flag_ca:"),
    MUMBAI("Mumbai", "MUM", 100, ":flag_in:"),
    NEW_YORK_CITY("New York City", "NYC", 30, ":flag_us:"),
    PARIS("Paris", "PAR", 100, ":flag_fr:"),
    ROME("Rome", "ROM", 100, ":flag_it"),
    SHANGHAI("Shanghai", "SH", 200, ":flag_cn:"),
    SYDNEY("Sydney", "SYD", 50, ":flag_au:"),
    TOKYO("Tokyo", "TYO", 125, ":flag_jp:");

    public final String name;
    public final String code;
    public final int price;
    public final String flag;

    Cities(String name, String code, int price, String flag) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.flag = flag;
    }
}
