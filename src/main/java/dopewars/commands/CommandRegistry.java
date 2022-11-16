package dopewars.commands;

import dopewars.DopeWars;
import dopewars.commands.economy.BalanceCommand;
import dopewars.commands.economy.DepositCommand;
import dopewars.commands.economy.WithdrawCommand;
import dopewars.commands.general.InventoryCommand;
import dopewars.commands.general.ProfileCommand;
import dopewars.commands.general.StartCommand;
import dopewars.commands.farming.GrowCommand;
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
     * @param bot An instance of DopeWars bot.
     */
    public CommandRegistry(DopeWars bot) {
        //General commands
        commands.add(new StartCommand(bot));
        commands.add(new ProfileCommand(bot));
        commands.add(new InventoryCommand(bot));

        //Farming commands
        commands.add(new GrowCommand(bot));

        //Economy commands
        commands.add(new BalanceCommand(bot));
        commands.add(new DepositCommand(bot));
        commands.add(new WithdrawCommand(bot));

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
