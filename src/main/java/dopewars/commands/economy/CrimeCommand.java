package dopewars.commands.economy;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.pojos.Player;
import dopewars.handlers.TimeoutHandler;
import dopewars.handlers.economy.EconomyReply;
import dopewars.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command that risks losing money for a potential reward.
 *
 * @author TechnoVision
 */
public class CrimeCommand extends Command {

    public static final TimeoutHandler.TimeoutType timeoutType = TimeoutHandler.TimeoutType.CRIME;

    public CrimeCommand(DopeWars bot) {
        super(bot);
        this.name = "crime";
        this.description = "Commit a crime for a chance at some cash.";
        this.category = Category.ECONOMY;
    }

    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        Player player = bot.playerHandler.getPlayer(user.getIdLong());

        if (bot.timeoutHandler.isOnTimeout(user.getIdLong(), timeoutType)) {
            // On timeout
            String cooldown = bot.timeoutHandler.getTimeout(user.getIdLong(), timeoutType);
            event.reply(":stopwatch: You already committed a crime, wait at least **" + cooldown + "**...").setEphemeral(true).queue();
        } else {
            // Commit crime
            EconomyReply reply = bot.economyHandler.crime(player);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor(event.getUser().getAsTag(), null, event.getUser().getEffectiveAvatarUrl());
            embed.setDescription(reply.getResponse());
            embed.setColor(reply.isSuccess() ? EmbedColor.SUCCESS.color : EmbedColor.ERROR.color);
            embed.setFooter("Reply #" + reply.getId());
            event.replyEmbeds(embed.build()).queue();
        }
    }
}
