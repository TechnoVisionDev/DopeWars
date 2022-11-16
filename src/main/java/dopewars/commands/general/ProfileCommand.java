package dopewars.commands.general;

import dopewars.DopeWars;
import dopewars.commands.Command;
import dopewars.data.pojos.Player;
import dopewars.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * Command that shows a player's profile stats.
 *
 * @author TechnoVision
 */
public class ProfileCommand extends Command {

    public ProfileCommand(DopeWars bot) {
        super(bot);
        this.name = "profile";
        this.description = "Display your profile stats";
        this.args.add(new OptionData(OptionType.USER, "user", "See another user's profile"));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Get user
        event.deferReply().queue();
        OptionMapping option = event.getOption("user");
        User user;
        if (option != null) {
            user = option.getAsUser();
            if (bot.cache.getPlayer(user.getIdLong()) == null) {
                String msg = "@"+event.getUser().getAsTag()+"! You cannot do this because **"+user.getName()+"** has never played!";
                event.getHook().sendMessage(msg).queue();
                return;
            }
        } else {
            user = event.getUser();
        }

        // Retrieve player profile from cache
        Player player = bot.cache.getPlayer(user.getIdLong());

        // Display in message embed
        String stats = ":gun: **AT**: "+ player.getAttack() +
                "\n:shield: **DEF**: "+ player.getDefense() +
                "\n:heart: **LIFE**: "+ player.getHealth() + "/" + player.getMaxHealth();

        String equipment = "No weapon" +
                "\nNo armor" +
                "\nNo vehicle";

        String money = ":dollar: **Cash**: "+ player.getCash() +
                "\n:bank: **Bank**: "+ player.getBank();

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getName()+"'s Profile", null, user.getEffectiveAvatarUrl())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("STATS", stats, false)
                .addField("EQUIPMENT", equipment, true)
                .addField("MONEY", money, true);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
