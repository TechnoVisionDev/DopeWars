package dopewars.commands.economy;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.Emojis.CURRENCY;
import static dopewars.util.Emojis.FAIL;

/**
 * Command that deposits cash into the bank.
 *
 * @author TechnoVision
 */
public class DepositCommand extends Command {

    public DepositCommand(DopeWars bot) {
        super(bot);
        this.name = "deposit";
        this.description = "Deposit your money to the bank.";
        this.category = Category.ECONOMY;
        this.args.add(new OptionData(OptionType.INTEGER, "amount", "The amount of money you want to deposit.").setMinValue(1));
    }

    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        Player player = bot.playerHandler.getPlayer(user.getIdLong());

        // Check if cash balance is 0
        long cash = player.getCash();
        if (cash <= 0) {
            event.reply(FAIL + " You don't have any cash to deposit!").setEphemeral(true).queue();
            return;
        }

        // Calculate amount to deposit
        OptionMapping amountOption = event.getOption("amount");
        long amount;
        if (amountOption != null) {
            amount = amountOption.getAsLong();
            if (amountOption.getAsLong() > cash) {
                // Amount is higher than balance
                String value = CURRENCY + " " + NUM_FORMAT.format(amount);
                event.reply(FAIL + " You cannot deposit more than " + value + "!").setEphemeral(true).queue();
                return;
            }
        } else {
            amount = cash;
        }

        // Deposit and send response message
        bot.economyHandler.deposit(player, amount);
        String value = CURRENCY + " " + NUM_FORMAT.format(amount);
        event.reply("**" + user.getName() + "** deposited " + value + " to their bank!").queue();
    }
}
