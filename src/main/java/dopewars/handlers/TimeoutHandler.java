package dopewars.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Stores and manages timeouts for commands.
 *
 * @author TechnoVision
 */
public class TimeoutHandler {

    private static final long ONE_MINUTE = 60 * 1000;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_MINUTE;

    private final Map<Long, Map<TimeoutType, Long>> timeouts;

    /**
     * Setup datastructures to hold timeouts.
     */
    public TimeoutHandler() {
        this.timeouts = new HashMap<>();
    }

    /**
     * Adds a new timeout for a specified command
     *
     * @param user_id the ID of the user who ran the command.
     * @param timeout the timeout type based on the command that was run.
     */
    public void addTimeout(long user_id, TimeoutType timeout) {
        timeouts.putIfAbsent(user_id, new HashMap<>());
        timeouts.get(user_id).put(timeout, System.currentTimeMillis() + timeout.time);
    }

    /**
     * Checks if a specific timeout type is currently on cooldown.
     *
     * @param user_id the ID of the user who ran the command.
     * @param timeout the timeout type based on the command that was run.
     * @return true if user is on timeout, otherwise false.
     */
    public boolean isOnTimeout(long user_id, TimeoutType timeout) {
        Map<TimeoutType, Long> userTimeouts = timeouts.get(user_id);
        if (userTimeouts != null) {
            Long timestamp = userTimeouts.get(timeout);
            if (timestamp != null) {
                return (timestamp - System.currentTimeMillis()) > 0;
            }
        }
        return false;
    }

    /**
     * Creates a string displaying the time left on a given timeout.
     *
     * @param user_id the ID of the user who ran the command.
     * @param timeout the timeout type based on the command that was run.
     * @return a string version displaying the time left on timeout.
     */
    public String getTimeout(long user_id, TimeoutType timeout) {
        Map<TimeoutType, Long> userTimeouts = timeouts.get(user_id);
        if (userTimeouts != null) {
            Long timestamp = userTimeouts.get(timeout);
            if (timestamp != null) {
                long cooldown = timestamp - System.currentTimeMillis();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(cooldown);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(cooldown) - TimeUnit.MINUTES.toSeconds(minutes);
                return minutes + "m " + seconds + "s";
            }
        }
        return "0m 0s";
    }

    /**
     * Represents the different types of timeouts available.
     * Each timeout is tied to a command of the same name.
     */
    public enum TimeoutType {
        GROW(15 * ONE_MINUTE),
        CRIME(4 * ONE_HOUR),
        ROB(4 * ONE_HOUR);

        public final long time;

        TimeoutType(long time) {
            this.time = time;
        }
    }
}
