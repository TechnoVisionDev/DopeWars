package civbot.commands.general;

import civbot.CivBot;
import civbot.commands.Command;
import civbot.util.EmbedColor;
import civbot.util.Professions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command that showcases information on all professions.
 *
 * @author TechnoVision
 */
public class ProfessionsCommand extends Command {

    public ProfessionsCommand(CivBot bot) {
        super(bot);
        this.name = "professions";
        this.description = "View information about all professions";
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setTitle("Professions")
                .setDescription("More information about a profession with `professions [profession]`\nYou can select your profession with `join [profession]`");
        for (Professions job : Professions.values()) {
            String name = job.name.substring(0, 1).toUpperCase() + job.name.substring(1);;
            embed.addField(job.emoji + " " + name, job.description, false);
        }
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
