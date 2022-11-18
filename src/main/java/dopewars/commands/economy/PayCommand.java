package dopewars.commands.economy;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.handlers.TimeoutHandler;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.enums.Emojis.*;

/**
 * Command that transfer cash from one user to another.
 *
 * @author TechnoVision
 */
public class PayCommand extends Command {

    public static final TimeoutHandler.TimeoutType timeoutType = TimeoutHandler.TimeoutType.ROB;

    public PayCommand(DopeWars bot) {
        super(bot);
        this.name = "pay";
        this.description = "Send cash to another user.";
        this.category = Category.ECONOMY;
        this.args.add(new OptionData(OptionType.USER, "user", "The user you want to rob.", true));
        this.args.add(new OptionData(OptionType.INTEGER, "amount", "The amount of money to send.", true).setMinValue(1));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Get user data
        User user = event.getUser();
        User targetUser = event.getOption("user").getAsUser();
        if (user.getIdLong() == targetUser.getIdLong()) {
            // Check for invalid target
            event.reply(FAIL + " You cannot pay yourself!").setEphemeral(true).queue();
            return;
        }
        if (targetUser.isBot()) {
            // Check if target is a bot
            event.reply(FAIL + " You cannot pay bots!").setEphemeral(true).queue();
            return;
        }

        // Get player data
        Player player = bot.playerHandler.getPlayer(user.getIdLong());
        Player target = bot.playerHandler.getPlayer(targetUser.getIdLong());
        if (target == null) {
            // Check if target is a valid player
            String msg = event.getUser().getAsMention()+" You cannot do this because **"+targetUser.getName()+"** has never played!";
            event.reply(msg).queue();
            return;
        }

        // Check that user has necessary funds
        long amount = event.getOption("amount").getAsLong();
        long cash = player.getCash();
        String value = NUM_FORMAT.format(cash) + " " + CURRENCY;
        if (amount > cash) {
            String msg = "You don't have that much money to give. You currently have " + value + " on hand.";
            event.reply(msg).setEphemeral(true).queue();
        }

        // Pay target and send message
        value = NUM_FORMAT.format(amount) + " " + CURRENCY;
        bot.economyHandler.pay(player, target, amount);
        event.reply(SUCCESS + " <@" + target.getId() + "> has received your " + value + ".").queue();
    }
}
