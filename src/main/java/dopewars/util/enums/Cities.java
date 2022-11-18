package dopewars.util.enums;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public enum Cities {

    AMSTERDAM("Amsterdam", "Europe/Amsterdam", 50, ":flag_nl:", "https://www.travelandleisure.com/thmb/_3nQ1ivxrnTKVphdp9ZYvukADKQ=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/amsterdam-nl-AMSTERDAMTG0521-6d2bfaac29704667a950bcf219680640.jpg"),
    BARCELONA("Barcelona", "Europe/Madrid", 75, ":flag_es:", "https://d3dqioy2sca31t.cloudfront.net/Projects/cms/production/000/020/484/original/d0531471711b367b94abfd4dbc29e6ae/spain-barcelona-sagrada-familia-080416-az.jpg"),
    BERLIN("Berlin", "Europe/Berlin", 45, ":flag_de:", "https://media.cntraveler.com/photos/5b914e80d5806340ca438db1/16:9/w_2580,c_limit/BrandenburgGate_2018_GettyImages-549093677.jpg"),
    LONDON("London", "Europe/London", 175, ":flag_gb:", "https://a.cdn-hotels.com/gdcs/production29/d1870/6a5ec560-bb25-11e8-970b-0242ac110006.jpg?impolicy=fcrop&w=800&h=533&q=medium"),
    LOS_ANGELES("Los Angeles", "America/Los_Angeles", 15, ":flag_us:", "https://a.travel-assets.com/findyours-php/viewfinder/images/res70/475000/475457-Los-Angeles.jpg"),
    MEXICO_CITY("Mexico City", "America/Mexico_City", 50, ":flag_mx:", "https://travel.home.sndimg.com/content/dam/images/travel/stock/2018/2/16/iStock_Zocalo-Square_Mexico-City_622286316_xl.jpg.rend.hgtvcom.1280.853.suffix/1518794349078.jpeg"),
    MONTREAL("Montreal", "America/Montreal", 90, ":flag_ca:", "https://upload.wikimedia.org/wikipedia/commons/2/2c/Montreal_-_QC_-_Skyline.jpg"),
    MUMBAI("Mumbai", "Asia/Kolkata", 100, ":flag_in:", "https://www.micato.com/wp-content/uploads/2018/09/Mumbai-1110x700.jpg"),
    NEW_YORK_CITY("New York City", "America/New_York", 30, ":flag_us:", "https://a.travel-assets.com/findyours-php/viewfinder/images/res70/104000/104059-New-York.jpg"),
    PARIS("Paris", "Europe/Paris", 100, ":flag_fr:", "https://www.planetware.com/photos-large/F/france-paris-eiffel-tower.jpg"),
    ROME("Rome", "Europe/Rome", 100, ":flag_it:", "https://www.fodors.com/wp-content/uploads/2018/10/HERO_UltimateRome_Hero_shutterstock789412159.jpg"),
    SHANGHAI("Shanghai", "Asia/Shanghai", 200, ":flag_cn:", "https://www.travelandleisure.com/thmb/b5vMz02HCnTV8TFbN3vvZ837bRE=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/shanghai-china-SHANGHAITG0721-c8b6b61c10204de891d5535fd607d243.jpg"),
    SYDNEY("Sydney", "Australia/Sydney", 50, ":flag_au:", "https://cdn.britannica.com/96/100196-050-C92064E0/Sydney-Opera-House-Port-Jackson.jpg"),
    TOKYO("Tokyo", "Asia/Tokyo", 125, ":flag_jp:", "https://www.gotokyo.org/en/plan/tokyo-outline/images/main.jpg");

    private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("h:mm a");

    public final String name;
    public final DateTime dateTime;
    public final int price;
    public final String flag;
    public final String thumbnail;

    Cities(String name, String timezone, int price, String flag, String thumbnail) {
        this.name = name;
        this.dateTime = new DateTime(DateTimeZone.forID(timezone));;
        this.price = price;
        this.flag = flag;
        this.thumbnail = thumbnail;
    }

    public String getTime() {
        return dateTime.toString(dateFormat);
    }
}
