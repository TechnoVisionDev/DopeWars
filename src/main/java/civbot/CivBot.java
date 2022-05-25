package civbot;

import civbot.commands.CommandRegistry;
import civbot.data.DatabaseManager;
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
public class CivBot extends ListenerAdapter {

    public final @NotNull Dotenv config;
    public final @NotNull ShardManager shardManager;
    public final @NotNull DatabaseManager databaseManager;

    /**
     * Builds bot shards and registers commands and modules.
     *
     * @throws LoginException throws if bot token is invalid.
     */
    public CivBot() throws LoginException {
        //Build JDA shards
        config = Dotenv.load();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("/help"));
        shardManager = builder.build();
        shardManager.addEventListener(new CommandRegistry(this));

        //Create Managers & Modules
        databaseManager = new DatabaseManager(config.get("DATABASE"));
    }

    /**
     * Initialize CivBot.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        CivBot bot;
        try {
            bot = new CivBot();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}
