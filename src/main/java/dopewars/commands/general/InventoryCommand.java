package dopewars.commands.general;

import dopewars.DopeWars;
import dopewars.commands.Command;
import dopewars.items.Drugs;
import dopewars.items.Equipment;
import dopewars.util.EmbedColor;
import dopewars.items.Materials;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Map;

public class InventoryCommand extends Command {

    public InventoryCommand(DopeWars bot) {
        super(bot);
        this.name = "inventory";
        this.description = "Display your items";
        this.args.add(new OptionData(OptionType.USER, "player", "See another user's inventory"));
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
        for (Map.Entry<String,Long> entry : bot.cache.getPlayer(user.getIdLong()).getMaterials().entrySet()) {
            try {
                Materials material = Materials.valueOf(entry.getKey().toUpperCase());
                materials.append(material.emoji)
                        .append(" ").append("**").append(material.name).append(":**")
                        .append(" ").append(entry.getValue())
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Retrieve equipment from cache
        StringBuilder equipment = new StringBuilder();
        for (Map.Entry<String,Long> entry : bot.cache.getPlayer(user.getIdLong()).getEquipment().entrySet()) {
            try {
                Equipment item = Equipment.valueOf(entry.getKey().toUpperCase());
                equipment.append(item.emoji)
                        .append(" ").append("**").append(item.name).append(":**")
                        .append(" ").append(entry.getValue())
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Retrieve drugs from cache
        StringBuilder drugs = new StringBuilder();
        for (Map.Entry<String,Long> entry : bot.cache.getPlayer(user.getIdLong()).getDrugs().entrySet()) {
            try {
                Drugs drug = Drugs.valueOf(entry.getKey().toUpperCase());
                drugs.append(drug.emoji)
                        .append(" ").append("**").append(drug.name).append(":**")
                        .append(" ").append(entry.getValue())
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Display in message embed
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getName()+"'s Inventory", null, user.getEffectiveAvatarUrl())
                .addField("Materials", materials.toString(), true)
                .addField("Equipment", equipment.toString(), true)
                .addField("Drugs", drugs.toString(), true);
        event.getHook().sendMessageEmbeds(embed.build()).queue();
    }
}
