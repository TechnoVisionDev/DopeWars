package dopewars.commands;

import dopewars.DopeWars;
import dopewars.commands.casino.BlackjackCommand;
import dopewars.commands.casino.CoinflipCommand;
import dopewars.commands.casino.CrashCommand;
import dopewars.commands.casino.SlotsCommand;
import dopewars.commands.economy.*;
import dopewars.commands.market.BuyCommand;
import dopewars.commands.market.SellCommand;
import dopewars.commands.market.ShopCommand;
import dopewars.commands.player.FlyCommand;
import dopewars.commands.player.InventoryCommand;
import dopewars.commands.player.ProfileCommand;
import dopewars.commands.player.StartCommand;
import dopewars.commands.farming.GrowCommand;
import dopewars.commands.utility.HelpCommand;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers, listens, and executes commands.
 *
 * @author TechnoVision
 */
public class CommandRegistry extends ListenerAdapter {

    /** List of commands in the exact order registered */
    public static final List<Command> commands = new ArrayList<>();

    /** Map of command names to command objects */
    public static final Map<String, Command> commandsMap = new HashMap<>();

    private final DopeWars bot;

    /**
     * Adds commands to a global list and registers them as event listener.
     *
     * @param bot An instance of DopeWars bot.
     */
    public CommandRegistry(DopeWars bot) {
        this.bot = bot;
        mapCommand(
                //Player
                new StartCommand(bot),
                new ProfileCommand(bot),
                new InventoryCommand(bot),
                new FlyCommand(bot),

                //Economy
                new BalanceCommand(bot),
                new CrimeCommand(bot),
                new RobCommand(bot),
                new DepositCommand(bot),
                new WithdrawCommand(bot),
                new PayCommand(bot),

                //Market
                new ShopCommand(bot),
                new BuyCommand(bot),
                new SellCommand(bot),

                // Casino
                new BlackjackCommand(bot),
                new CrashCommand(bot),
                new SlotsCommand(bot),
                new CoinflipCommand(bot),

                //Farming
                new GrowCommand(bot),

                // Utility
                new HelpCommand(bot)
        );
    }

    /**
     * Adds a command to the static list and map.
     *
     * @param cmds a spread list of command objects.
     */
    private void mapCommand(Command ...cmds) {
        for (Command cmd : cmds) {
            commandsMap.put(cmd.name, cmd);
            commands.add(cmd);
        }
    }

    /**
     * Creates a list of CommandData for all commands.
     *
     * @return a list of CommandData to be used for registration.
     */
    public static List<CommandData> unpackCommandData() {
        // Register slash commands
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commands) {
            SlashCommandData slashCommand = Commands.slash(command.name, command.description).addOptions(command.args);
            if (!command.subCommands.isEmpty()) {
                slashCommand.addSubcommands(command.subCommands);
            }
            commandData.add(slashCommand);
        }
        return commandData;
    }

    /**
     * Runs whenever a slash command is run in Discord.
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        // Get command by name
        Command cmd = commandsMap.get(event.getName());
        if (cmd != null) {
            if (event.getName().equalsIgnoreCase("start") || bot.playerHandler.getPlayer(event.getUser().getIdLong()) != null) {
                // Run command
                cmd.execute(event);
            } else {
                // Player has not started game
                String msg = "You must begin your journey with **/start** before using that command!";
                event.reply(msg).queue();
            }
        }
    }

    /**
     * Registers slash commands as guild commands.
     * NOTE: May change to global commands on release.
     *
     * @param event executes when a guild is ready.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue(succ -> {}, fail -> {});
    }
}
