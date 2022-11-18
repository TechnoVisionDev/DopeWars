package dopewars.commands.player;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.util.enums.Cities;
import dopewars.util.enums.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.DopeWars.NUM_FORMAT;

/**
 * Command that shows a player's profile stats.
 *
 * @author TechnoVision
 */
public class ProfileCommand extends Command {

    public ProfileCommand(DopeWars bot) {
        super(bot);
        this.name = "profile";
        this.description = "Display your profile stats.";
        this.category = Category.PLAYER;
        this.args.add(new OptionData(OptionType.USER, "user", "See another user's profile"));
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

        // Display in message embed
        Cities city = Cities.valueOf(player.getCity());
        String progress = ":chart_with_upwards_trend: **Level:** "+ NUM_FORMAT.format(player.getLevel()) +
                "\n:medal: **Prestige**: "+ NUM_FORMAT.format(player.getPrestige()) +
                "\n"+ city.flag +" **City**: "+ city.name;

        String stats = ":dagger: **AT**: "+ NUM_FORMAT.format(player.getAttack()) +
                "\n:shield: **DEF**: "+ NUM_FORMAT.format(player.getDefense()) +
                "\n:heart: **LIFE**: "+ NUM_FORMAT.format(player.getHealth()) +
                "/" + NUM_FORMAT.format(player.getMaxHealth()) +
                "\n:jar: **INV**: "+ NUM_FORMAT.format(bot.itemHandler.getInventoryCount(player)) +
                "/" + NUM_FORMAT.format(player.getStorage());

        String equipment = ":x: No weapon" +
                "\n:x: No armor" +
                "\n:x: No vehicle";

        String money = ":dollar: **Cash**: "+ NUM_FORMAT.format(player.getCash()) +
                "\n:bank: **Bank**: "+ NUM_FORMAT.format(player.getBank());

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getName()+"'s Profile", null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("PROGRESS", progress, false)
                .addField("STATS", stats, false)
                .addField("EQUIPMENT", equipment, true)
                .addField("MONEY", money, true);
        event.replyEmbeds(embed.build()).queue();
    }
}
