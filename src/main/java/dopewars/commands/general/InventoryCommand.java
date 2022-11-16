package dopewars.commands.general;

import dopewars.DopeWars;
import dopewars.commands.Command;
import dopewars.data.pojos.Player;
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

import static dopewars.DopeWars.NUM_FORMAT;

public class InventoryCommand extends Command {

    public InventoryCommand(DopeWars bot) {
        super(bot);
        this.name = "inventory";
        this.description = "Display your items";
        this.args.add(new OptionData(OptionType.USER, "user", "See another user's inventory"));
    }

    @Override
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

        // Retrieve materials from cache
        StringBuilder materials = new StringBuilder();
        for (Map.Entry<String,Long> entry : player.getMaterials().entrySet()) {
            try {
                Materials material = Materials.valueOf(entry.getKey().toUpperCase());
                materials.append(material.emoji)
                        .append(" ").append("**").append(material.name).append(":**")
                        .append(" ").append(NUM_FORMAT.format(entry.getValue()))
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Retrieve equipment from cache
        StringBuilder equipment = new StringBuilder();
        for (Map.Entry<String,Long> entry : player.getEquipment().entrySet()) {
            try {
                Equipment item = Equipment.valueOf(entry.getKey().toUpperCase());
                equipment.append(item.emoji)
                        .append(" ").append("**").append(item.name).append(":**")
                        .append(" ").append(NUM_FORMAT.format(entry.getValue()))
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Retrieve drugs from cache
        StringBuilder drugs = new StringBuilder();
        for (Map.Entry<String,Long> entry : player.getDrugs().entrySet()) {
            try {
                Drugs drug = Drugs.valueOf(entry.getKey().toUpperCase());
                drugs.append(drug.emoji)
                        .append(" ").append("**").append(drug.name).append(":**")
                        .append(" ").append(NUM_FORMAT.format(entry.getValue()))
                        .append("\n");
            } catch (IllegalArgumentException ignored) { }
        }

        // Display in message embed
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getName()+"'s Inventory", null, user.getEffectiveAvatarUrl())
                .addField("Materials", materials.toString(), true)
                .addField("Drugs", drugs.toString(), true)
                .addField("Equipment", equipment.toString(), true);
        event.replyEmbeds(embed.build()).queue();
    }
}
