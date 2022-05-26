package civbot.commands.general;

import civbot.CivBot;
import civbot.commands.Command;
import civbot.data.pojos.Player;
import civbot.util.EmbedColor;
import civbot.util.Materials;
import civbot.util.Professions;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Locale;
import java.util.Map;

public class InventoryCommand extends Command {

    public InventoryCommand(CivBot bot) {
        super(bot);
        this.name = "inventory";
        this.description = "Display your items";
        this.args.add(new OptionData(OptionType.USER, "user", "See another player's inventory"));
    }

    @Override
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

        // Retrieve materials from cache
        StringBuilder materials = new StringBuilder();
        for (Map.Entry<String,Long> entry : bot.cache.getPlayer(user.getIdLong()).getInventory().entrySet()) {
            try {
                Materials material = Materials.valueOf(entry.getKey().toUpperCase());
                materials.append(material.emoji)
                        .append(" ").append("**").append(material.name).append(":**")
                        .append(" ").append(entry.getValue())
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Display in message embed
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getName()+"'s Inventory", null, user.getEffectiveAvatarUrl())
                .addField("Materials", materials.toString(), true)
                .addField("Items", "", true)
                .addField("Consumables", "", true);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
