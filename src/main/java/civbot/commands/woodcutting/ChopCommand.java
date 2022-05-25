package civbot.commands.woodcutting;

import civbot.CivBot;
import civbot.commands.Command;
import civbot.util.Materials;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Woodcutting command that gives the player 1-3 wooden logs.
 * Acts as an "action" and thus has a cooldown timer.
 *
 * @author TechnoVision
 */
public class ChopCommand extends Command {

    public ChopCommand(CivBot bot) {
        super(bot);
        this.name = "chop";
        this.description = "Chop a tree [woodcutter]";
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        int amount;
        int rand = ThreadLocalRandom.current().nextInt(11);
        if (rand <= 5) {
            amount = 1;
        } else if (rand <= 8) {
            amount = 2;
        } else {
            amount = 3;
        }
        String username = event.getUser().getName();
        String name = Materials.WOODEN_LOG.name;
        String emoji = Materials.WOODEN_LOG.emoji;
        event.getHook().sendMessage("**"+username+"** got "+amount+" "+emoji+" "+name).queue();
    }
}
