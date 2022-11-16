package dopewars;

import dopewars.commands.CommandRegistry;
import dopewars.data.Cache;
import dopewars.data.DatabaseManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

/**
 * Discord bot built with JDA.
 *
 * @author TechnVision
 * @version 1.0
 */
public class DopeWars extends ListenerAdapter {

    public final @NotNull Dotenv config;
    public final @NotNull ShardManager shardManager;
    public final @NotNull DatabaseManager databaseManager;
    public final @NotNull Cache cache;

    /**
     * Builds bot shards and registers commands and modules.
     *
     * @throws LoginException throws if bot token is invalid.
     */
    public DopeWars() throws LoginException {
        //Build JDA shards
        config = Dotenv.load();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("/help"));
        shardManager = builder.build();
        shardManager.addEventListener(new CommandRegistry(this));

        //Create Managers & Modules
        databaseManager = new DatabaseManager(config.get("DATABASE"));
        cache = new Cache(databaseManager);
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
