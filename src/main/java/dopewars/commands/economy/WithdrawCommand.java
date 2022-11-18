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
import static dopewars.util.enums.Emojis.CURRENCY;
import static dopewars.util.enums.Emojis.FAIL;

/**
 * Command that withdraws cash from the bank.
 *
 * @author TechnoVision
 */
public class WithdrawCommand extends Command {

    public WithdrawCommand(DopeWars bot) {
        super(bot);
        this.name = "withdraw";
        this.description = "Withdraw your money from the bank.";
        this.category = Category.ECONOMY;
        this.args.add(new OptionData(OptionType.INTEGER, "amount", "The amount of money you want to withdraw.").setMinValue(1));
    }

    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        Player player = bot.playerHandler.getPlayer(user.getIdLong());

        // Check if bank balance is 0
        long bank = player.getBank();
        if (bank <= 0) {
            event.reply(FAIL + " You don't have any cash to withdraw!").setEphemeral(true).queue();
            return;
        }

        // Calculate amount to withdraw
        OptionMapping amountOption = event.getOption("amount");
        long amount;
        if (amountOption != null) {
            amount = amountOption.getAsLong();
            if (amountOption.getAsLong() > bank) {
                // Amount is higher than bank balance
                String value = NUM_FORMAT.format(bank) + " " + CURRENCY;
                event.reply(FAIL + " You cannot withdraw more than " + value + "!").setEphemeral(true).queue();
                return;
            }
        } else {
            amount = bank;
        }

        // Deposit and send response message
        bot.economyHandler.withdraw(player, amount);
        String value = NUM_FORMAT.format(amount) + " " + CURRENCY;
        event.reply("**" + user.getName() + "** withdrew " + value + " from their bank!").queue();
    }
}
