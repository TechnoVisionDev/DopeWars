package dopewars.commands;

import dopewars.DopeWars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a general slash command with properties.
 *
 * @author TechnoVision
 */
public abstract class Command {

    protected DopeWars bot;
    protected String name;
    protected String description;
    protected Category category;
    protected List<OptionData> args;
    public List<SubcommandData> subCommands;

    public Command(DopeWars bot) {
        this.bot = bot;
        this.args = new ArrayList<>();
        this.subCommands = new ArrayList<>();
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public List<OptionData> getArgs() {
        return args;
    }

    public List<SubcommandData> getSubCommands() {
        return subCommands;
    }
}
