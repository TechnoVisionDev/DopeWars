package civbot.commands.general;

import civbot.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command that creates user profile in database and displays tutorial.
 *
 * @author TechnoVision
 */
public class StartCommand extends Command {

    public StartCommand() {
        this.name = "start";
        this.description = "Start your journey";
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.reply("You have started your journey!").queue();
    }
}
