package civbot.commands;

import civbot.CivBot;
import civbot.commands.general.InventoryCommand;
import civbot.commands.general.JoinCommand;
import civbot.commands.general.ProfessionsCommand;
import civbot.commands.general.StartCommand;
import civbot.commands.woodcutting.ChopCommand;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers, listens, and executes commands.
 *
 * @author TechnoVision
 */
public class CommandRegistry extends ListenerAdapter {

    public static final ArrayList<Command> commands = new ArrayList<>();

    /**
     * Adds commands to a global list and registers them as event listener.
     *
     * @param bot An instance of CivBot.
     */
    public CommandRegistry(CivBot bot) {
        //General commands
        commands.add(new StartCommand(bot));
        commands.add(new ProfessionsCommand(bot));
        commands.add(new JoinCommand(bot));
        commands.add(new InventoryCommand(bot));

        //Woodcutting commands
        commands.add(new ChopCommand(bot));

        //Register commands as listeners
        for (Command command : commands) {
            bot.shardManager.addEventListener(command);
        }
    }

    /**
     * Registers slash commands as guild commands.
     * TEMPORARY! CHANGE TO GLOBAL COMMANDS ON RELEASE!
     *
     * @param event executes when a guild is ready.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commands) {
            commandData.add(Commands.slash(command.name, command.description).addOptions(command.args));
        }
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
