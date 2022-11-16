package dopewars.commands.farming;

import dopewars.DopeWars;
import dopewars.commands.Command;
import dopewars.handlers.TimeoutHandler;
import dopewars.items.ItemTypes;
import dopewars.items.Plants;
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

    public static final TimeoutHandler.Timeouts timeoutType = TimeoutHandler.Timeouts.GROW;

    public GrowCommand(DopeWars bot) {
        super(bot);
        this.name = "grow";
        this.description = "Grow various plants for resources";
        OptionData data = new OptionData(OptionType.STRING, "plant", "the plant you want to grow", true);
        for (Plants plant : Plants.values()) {
            data.addChoice(plant.name, plant.toString().toUpperCase());
        }
        this.args.add(data);
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Check if command is on timeout
        event.deferReply().queue();
        User user = event.getUser();
        if (bot.timeoutHandler.isOnTimeout(user.getIdLong(), timeoutType)) {
            // Display remaining timeout
            String cooldown = bot.timeoutHandler.getTimeout(user.getIdLong(), timeoutType);
            event.getHook().sendMessage("You already grew some plants, wait at least **"+cooldown+"**...").queue();
            return;
        }

        // Get selected plant type
        String plantKey = event.getOption("plant").getAsString();
        Plants plant = Plants.valueOf(plantKey);

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
        ItemTypes itemType = ItemTypes.MATERIALS;
        if (plant == Plants.CANNABIS || plant == Plants.MUSHROOMS) {
            itemType = ItemTypes.DRUGS;
        }
        bot.playerHandler.addItem(user.getIdLong(), plantKey, amount, itemType);
        bot.timeoutHandler.addTimeout(user.getIdLong(), timeoutType);

        // Reply to user with message
        String username = user.getName();
        String name = plant.name;
        String emoji = plant.emoji;
        event.getHook().sendMessage("**" + username + "** got " + amount + " " + emoji + " " + name).queue();
    }
}
