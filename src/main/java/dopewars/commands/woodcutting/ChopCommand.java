package dopewars.commands.woodcutting;

import dopewars.DopeWars;
import dopewars.commands.Command;
import dopewars.util.Materials;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Woodcutting command that gives the player 1-3 wooden logs.
 * Acts as an "action" and thus has a cooldown timer.
 *
 * @author TechnoVision
 */
public class ChopCommand extends Command {

    public ChopCommand(DopeWars bot) {
        super(bot);
        this.name = "chop";
        this.description = "Chop a tree [woodcutter]";
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        // Generate amount randomly
        int amount;
        int rand = ThreadLocalRandom.current().nextInt(11);
        if (rand <= 5) {
            amount = 1;
        } else if (rand <= 8) {
            amount = 2;
        } else {
            amount = 3;
        }

        // Update cache and database
        User user = event.getUser();
        boolean result = bot.cache.addItem(user.getIdLong(), "oak_log", amount);

        if (result) {
            // Reply to user with message
            String username = user.getName();
            String name = Materials.OAK_LOG.name;
            String emoji = Materials.OAK_LOG.emoji;
            event.getHook().sendMessage("**" + username + "** got " + amount + " " + emoji + " " + name).queue();
        } else {
            String cooldown = bot.cache.getGatherCooldown(user.getIdLong());
            event.getHook().sendMessage("You already gathered some resources, wait at least **"+cooldown+"**...").queue();
        }
    }
}
