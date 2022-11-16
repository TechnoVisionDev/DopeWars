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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.util.Emojis.FAIL;

/**
 * Command that steals money from another player.
 *
 * @author TechnoVision
 */
public class RobCommand extends Command {

    public static final TimeoutHandler.TimeoutType timeoutType = TimeoutHandler.TimeoutType.ROB;

    public RobCommand(DopeWars bot) {
        super(bot);
        this.name = "rob";
        this.description = "Attempt to steal money from another user.";
        this.category = Category.ECONOMY;
        this.args.add(new OptionData(OptionType.USER, "user", "The user you want to rob.", true));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Get user data
        User user = event.getUser();
        User targetUser = event.getOption("user").getAsUser();
        if (user.getIdLong() == targetUser.getIdLong()) {
            // Check for invalid target
            event.reply(FAIL + " You cannot rob yourself!").setEphemeral(true).queue();
            return;
        }
        if (targetUser.isBot()) {
            // Check if target is a bot
            event.reply(FAIL + " You cannot rob bots, they are too powerful for you!").setEphemeral(true).queue();
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

        if (bot.timeoutHandler.isOnTimeout(user.getIdLong(), timeoutType)) {
            // On timeout
            String cooldown = bot.timeoutHandler.getTimeout(user.getIdLong(), timeoutType);
            event.reply(":stopwatch: You already robbed someone, wait at least **" + cooldown + "**...").setEphemeral(true).queue();
        } else {
            // Start robbery
            EconomyReply reply = bot.economyHandler.rob(player, target);
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(reply.isSuccess() ? EmbedColor.SUCCESS.color : EmbedColor.ERROR.color);
            embed.setDescription(reply.getResponse());
            event.replyEmbeds(embed.build()).queue();
        }
    }
}
