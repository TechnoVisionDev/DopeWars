package dopewars.handlers.economy;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.ThreadLocalRandom;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.enums.Emojis.CURRENCY;

/**
 * Handles localized responses to economy commands.
 *
 * @author TechnoVision
 */
public class EconomyLocalization {

    private static final String PATH = "localization/economy.json";

    private final String[] crimeSuccess;
    private final String[] crimeFail;

    /**
     * Reads economy.json responses into local memory
     */
    public EconomyLocalization() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PATH);
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        EconomyResponses responses = new Gson().fromJson(reader, EconomyResponses.class);
        crimeSuccess = responses.getCrimeSuccess();
        crimeFail = responses.getCrimeFail();
    }

    /**
     * Get a reply from the list of 'crimeSuccess' responses.
     *
     * @param amount the amount of money earned.
     * @return an EconomyReply object with response and ID number.
     */
    public EconomyReply getCrimeSuccessResponse(long amount) {
        int index = ThreadLocalRandom.current().nextInt(crimeSuccess.length);
        String value = CURRENCY+" "+NUM_FORMAT.format(amount);
        String reply = crimeSuccess[index].replaceAll("\\{amount}", value);
        return new EconomyReply(reply, index+1, true);
    }

    /**
     * Get a reply from the list of 'crimeFail' responses.
     *
     * @param amount the amount of money list.
     * @return an EconomyReply object with response and ID number.
     */
    public EconomyReply getCrimeFailResponse(long amount) {
        int index = ThreadLocalRandom.current().nextInt(crimeFail.length);
        String value = CURRENCY+" "+NUM_FORMAT.format(amount);
        String reply = crimeFail[index].replaceAll("\\{amount}", value);
        return new EconomyReply(reply, index+1, false);
    }

    /**
     * Represents list of responses to economy commands.
     * Used by OkHttp and Gson to convert JSON to java code.
     *
     * @author TechnoVision
     */
    public class EconomyResponses {

        private final String[] crimeSuccess;
        private final String[] crimeFail;

        public EconomyResponses(String[] crimeSuccess, String[] crimeFail) {
            this.crimeSuccess = crimeSuccess;
            this.crimeFail = crimeFail;
        }

        public String[] getCrimeSuccess() {
            return crimeSuccess;
        }

        public String[] getCrimeFail() {
            return crimeFail;
        }
    }
}
