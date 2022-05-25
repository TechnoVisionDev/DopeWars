package civbot.commands;

import civbot.CivBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a general slash command with properties.
 *
 * @author TechnoVision
 */
public abstract class Command extends ListenerAdapter {

    protected CivBot bot;
    protected String name;
    protected String description;

    public Command(CivBot bot) {
        this.bot = bot;
    }

    public abstract void execute(SlashCommandInteractionEvent event);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            execute(event);
        }
    }
}
