package dopewars.commands.player;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.data.items.Drug;
import dopewars.data.items.Equipment;
import dopewars.data.items.Material;
import dopewars.util.enums.EmbedColor;
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
        this.description = "Display your items.";
        this.category = Category.PLAYER;
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

        // Build inventory
        StringBuilder materials = new StringBuilder();
        StringBuilder drugs = new StringBuilder();
        StringBuilder equipments = new StringBuilder();

        for (Map.Entry<String,Long> entry : player.getInventory().entrySet()) {
            if (entry.getValue() == 0) continue;

            Material material = bot.itemHandler.getMaterial(entry.getKey());
            if (material != null) {
                materials.append(material.getEmoji())
                        .append(" ").append("**").append(material.getName()).append(":**")
                        .append(" ").append(NUM_FORMAT.format(entry.getValue()))
                        .append("\n");
                continue;
            }

            Drug drug = bot.itemHandler.getDrug(entry.getKey());
            if (drug != null) {
                drugs.append(drug.getEmoji())
                        .append(" ").append("**").append(drug.getName()).append(":**")
                        .append(" ").append(NUM_FORMAT.format(entry.getValue()))
                        .append("\n");
            }

            Equipment equipment = bot.itemHandler.getEquipment(entry.getKey());
            if (equipment != null) {
                equipments.append(equipment.getEmoji())
                        .append(" ").append("**").append(equipment.getName()).append(":**")
                        .append(" ").append(NUM_FORMAT.format(entry.getValue()))
                        .append("\n");
            }
        }

        // Display inventory in message embed
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getName()+"'s Inventory", null, user.getEffectiveAvatarUrl())
                .addField("Drugs", drugs.toString(), true)
                .addField("Materials", materials.toString(), true)
                .addField("Equipment", equipments.toString(), true)
                .setFooter(NUM_FORMAT.format(bot.itemHandler.getInventoryCount(player))+"/"+NUM_FORMAT.format(player.getStorage())+" Items");
        event.replyEmbeds(embed.build()).queue();
    }
}
