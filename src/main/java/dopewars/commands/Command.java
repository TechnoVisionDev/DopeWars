package dopewars.commands;

import dopewars.DopeWars;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a general slash command with properties.
 *
 * @author TechnoVision
 */
public abstract class Command extends ListenerAdapter {

    protected DopeWars bot;
    protected String name;
    protected String description;
    protected List<OptionData> args;

    public Command(DopeWars bot) {
        this.bot = bot;
        this.args = new ArrayList<>();
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            if (event.getName().equalsIgnoreCase("start") || bot.playerHandler.getPlayer(event.getUser().getIdLong()) != null) {
                execute(event);
            } else {
                String msg = "You must begin your journey with **/start** before using that command!";
                event.reply(msg).queue();
            }
        }
    }
}
