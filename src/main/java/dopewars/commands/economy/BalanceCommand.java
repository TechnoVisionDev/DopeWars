package dopewars.commands.economy;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.util.enums.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.enums.Emojis.CURRENCY;

/**
 * Command that shows a player's balance.
 *
 * @author TechnoVision
 */
public class BalanceCommand extends Command {

    public BalanceCommand(DopeWars bot) {
        super(bot);
        this.name = "balance";
        this.description = "Display your current balance.";
        this.category = Category.ECONOMY;
        this.args.add(new OptionData(OptionType.USER, "user", "See another user's balance"));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Get user and player
        OptionMapping userOption = event.getOption("user");
        User user = (userOption != null) ? userOption.getAsUser() : event.getUser();
        Player player = bot.playerHandler.getPlayer(user.getIdLong());
        if (player == null) {
            String msg = event.getUser().getAsMention()+" You cannot do this because **"+user.getName()+"** has never played!";
            event.reply(msg).queue();
            return;
        }

        // Get balance and bank values
        Long cash = player.getCash();
        Long bank = player.getBank();
        Long total = cash + bank;

        // Send embed message
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription("Leaderboard Rank: #1")
                .addField("Cash:", CURRENCY + " " + NUM_FORMAT.format(cash), true)
                .addField("Bank:", CURRENCY + " " + NUM_FORMAT.format(bank), true)
                .addField("Total:", CURRENCY + " " + NUM_FORMAT.format(total), true)
                .setColor(EmbedColor.DEFAULT.color);
        event.replyEmbeds(embed.build()).queue();
    }
}
