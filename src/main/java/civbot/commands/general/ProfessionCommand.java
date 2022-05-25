package civbot.commands.general;

import civbot.CivBot;
import civbot.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command that showcases information on all professions.
 *
 * @author TechnoVision
 */
public class ProfessionCommand extends Command {

    public ProfessionCommand(CivBot bot) {
        super(bot);
        this.name = "professions";
        this.description = "View information about all professions";
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        event.getHook().sendMessage("ALL PROFESSIONS:").queue();
    }
}
