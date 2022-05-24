package civbot.commands;

import civbot.commands.general.StartCommand;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Registers, listens, and executes commands.
 *
 * @author TechnoVision
 */
public class CommandRegistry extends ListenerAdapter {

    public static final ArrayList<Command> commands = new ArrayList<>();

    /**
     * Adds commands to a global list and registers them as event listener.
     * @param shardManager the shardManager created in CivBot.java
     */
    public CommandRegistry(ShardManager shardManager) {
        commands.add(new StartCommand());
        for (Command command : commands) {
            shardManager.addEventListener(command);
        }
    }

    /**
     * Registers slash commands as guild commands.
     * TEMPORARY! CHANGE TO GLOBAL COMMANDS ON RELEASE!
     * @param event
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        for (Command command : commands) {
            event.getGuild().upsertCommand(command.name, command.description).queue();
        }
    }
}
