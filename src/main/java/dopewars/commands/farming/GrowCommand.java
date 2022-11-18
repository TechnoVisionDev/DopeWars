package dopewars.commands.farming;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.handlers.TimeoutHandler;
import dopewars.data.items.Item;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Gives the player a random amount of a selected plant.
 * Acts as an "action" and thus has a timeout.
 *
 * @author TechnoVision
 */
public class GrowCommand extends Command {

    public static final TimeoutHandler.TimeoutType timeoutType = TimeoutHandler.TimeoutType.GROW;

    public GrowCommand(DopeWars bot) {
        super(bot);
        this.name = "grow";
        this.description = "Grow plants for resources.";
        this.category = Category.FARMING;
        OptionData data = new OptionData(OptionType.STRING, "plant", "the plant you want to grow", true);
        for (Item plant : bot.itemHandler.getPlants()) {
            data.addChoice(plant.getName(), plant.getName());
        }
        this.args.add(data);
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Check if command is on timeout
        User user = event.getUser();
        if (bot.timeoutHandler.isOnTimeout(user.getIdLong(), timeoutType)) {
            // Display remaining timeout
            String cooldown = bot.timeoutHandler.getTimeout(user.getIdLong(), timeoutType);
            event.reply(":stopwatch: You already grew some plants, wait at least **"+cooldown+"**...").setEphemeral(true).queue();
            return;
        }

        // Get selected plant type
        String plantName = event.getOption("plant").getAsString();
        Item plant = bot.itemHandler.getItem(plantName);

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

        // Add item to player's inventory and enable timeout
        Player player = bot.playerHandler.getPlayer(user.getIdLong());
        bot.itemHandler.addItem(player, plant.getName(), amount);
        bot.timeoutHandler.addTimeout(user.getIdLong(), timeoutType);

        // Reply to user with message
        String username = user.getName();
        String name = plant.getName();
        String emoji = plant.getEmoji();
        event.reply("**" + username + "** grew " + amount + " " + emoji + " " + name).queue();
    }
}
