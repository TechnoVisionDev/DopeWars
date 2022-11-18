package dopewars.commands.casino;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.util.enums.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.enums.Emojis.CURRENCY;
import static dopewars.util.enums.Emojis.FAIL;

/**
 * Command that plays a coinflip casino game.
 *
 * @author TechnoVision
 */
public class CoinflipCommand extends Command {

    public CoinflipCommand(DopeWars bot) {
        super(bot);
        this.name = "coinflip";
        this.description = "Flip a coin and bet on heads or tails.";
        this.category = Category.CASINO;
        this.args.add(new OptionData(OptionType.STRING, "choice", "The side you think the coin will land on", true)
                .addChoice("heads", "heads")
                .addChoice("tails", "tails"));
        this.args.add(new OptionData(OptionType.INTEGER, "bet", "The amount you want to wager", true).setMinValue(1));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Get command data
        User user = event.getUser();
        String choice = event.getOption("choice").getAsString();
        long bet = event.getOption("bet").getAsLong();
        Player player = bot.playerHandler.getPlayer(user.getIdLong());

        // Charge player for bet
        long balance = player.getCash();
        if (balance < bet) {
            String currency = CURRENCY + " **" + balance + "**";
            String text = FAIL + " You don't have enough money for this bet. You currently have " + currency + " in cash.";
            event.reply(text).setEphemeral(true).queue();
            return;
        }
        bot.economyHandler.removeMoney(player, bet);

        // Flip coin and calculate result
        EmbedBuilder embed = new EmbedBuilder();
        int result = ThreadLocalRandom.current().nextInt(2);
        String winnings = CURRENCY + " " + NUM_FORMAT.format(bet*2);
        String losings = CURRENCY + " -" + NUM_FORMAT.format(bet);
        if (result == 0) {
            embed.setAuthor("Heads!", null, user.getEffectiveAvatarUrl());
            if (choice.equalsIgnoreCase("heads")) {
                embed.setColor(EmbedColor.SUCCESS.color);
                embed.setDescription("Congratulations, the coin landed on heads!\nYou won " + winnings);
                bot.economyHandler.addMoney(player, bet*2);
            } else {
                embed.setColor(EmbedColor.ERROR.color);
                embed.setDescription("Sorry, the coin landed on heads.\nYou lost " + losings);
            }
        } else {
            embed.setAuthor("Tails!", null, user.getEffectiveAvatarUrl());
            if (choice.equalsIgnoreCase("tails")) {
                embed.setColor(EmbedColor.SUCCESS.color);
                embed.setDescription("Congratulations, the coin landed on tails!\nYou won " + winnings);
                bot.economyHandler.addMoney(player, bet*2);
            } else {
                embed.setColor(EmbedColor.ERROR.color);
                embed.setDescription("Sorry, the coin landed on tails.\nYou lost " + losings);
            }
        }

        // Send message response
        event.reply("<a:coinflip:993258934909550592> The coin flips into the air...").queue(msg -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    msg.editOriginalEmbeds(embed.build()).queue();
                }
            }, 2500L);
        });
    }
}
