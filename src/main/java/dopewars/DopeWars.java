package dopewars;

import dopewars.commands.CommandRegistry;
import dopewars.handlers.ItemHandler;
import dopewars.handlers.MarketHandler;
import dopewars.handlers.economy.EconomyHandler;
import dopewars.handlers.PlayerHandler;
import dopewars.data.DatabaseManager;
import dopewars.handlers.TimeoutHandler;
import dopewars.listeners.ButtonListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.text.DecimalFormat;

/**
 * Discord bot built with JDA.
 *
 * @author TechnVision
 * @version 1.0
 */
public class DopeWars extends ListenerAdapter {

    public static final DecimalFormat NUM_FORMAT = new DecimalFormat("#,###");

    public final @NotNull Dotenv config;
    public final @NotNull ShardManager shardManager;
    public final @NotNull DatabaseManager databaseManager;
    public final @NotNull PlayerHandler playerHandler;
    public final @NotNull ItemHandler itemHandler;
    public final @NotNull TimeoutHandler timeoutHandler;
    public final @NotNull EconomyHandler economyHandler;
    public final @NotNull MarketHandler marketHandler;

    /**
     * Builds bot shards and registers commands and modules.
     *
     * @throws LoginException throws if bot token is invalid.
     */
    public DopeWars() throws LoginException {
        //Create Managers & Modules
        config = Dotenv.load();
        databaseManager = new DatabaseManager(config.get("DATABASE"));
        playerHandler = new PlayerHandler(this);
        itemHandler = new ItemHandler(this);
        timeoutHandler = new TimeoutHandler();
        economyHandler = new EconomyHandler(this);
        marketHandler = new MarketHandler(this);

        //Build JDA shards
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("/help"));
        shardManager = builder.build();
        shardManager.addEventListener(new CommandRegistry(this), new ButtonListener(this));
    }

    /**
     * Initialize DopeWars bot.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        DopeWars bot;
        try {
            bot = new DopeWars();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}
