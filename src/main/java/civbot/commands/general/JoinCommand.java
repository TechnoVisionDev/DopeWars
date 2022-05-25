package civbot.commands.general;

import civbot.CivBot;
import civbot.commands.Command;
import civbot.util.Professions;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Command that allows users to join a profession.
 *
 * @author TechnoVision
 */
public class JoinCommand extends Command {

    public JoinCommand(CivBot bot) {
        super(bot);
        this.name = "join";
        this.description = "Join a profession";
        OptionData data = new OptionData(OptionType.STRING, "profession", "the profession you want to join", true);
        for (Professions job : Professions.values()) {
            data.addChoice(job.name, job.name);
        }
        this.args.add(data);
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        OptionMapping option = event.getOption("profession");
        if (option != null) {
            event.getHook().sendMessage(option.getAsString()).queue();
        }
    }
}
