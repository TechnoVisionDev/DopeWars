package dopewars.commands.market;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.items.Drugs;
import dopewars.items.Materials;
import dopewars.listeners.ButtonListener;
import dopewars.util.Cities;
import dopewars.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.ArrayList;
import java.util.List;

import static dopewars.util.Emojis.CURRENCY;

/**
 * Command that displays the server shop and available items.
 *
 * @author TechnoVision
 */
public class ShopCommand extends Command {

    public ShopCommand(DopeWars bot) {
        super(bot);
        this.name = "shop";
        this.description = "View items available to buy and sell for this server.";
        this.category = Category.MARKET;
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Create base embed template
        Player player = bot.playerHandler.getPlayer(event.getUser().getIdLong());
        Cities city = Cities.valueOf(player.getCity());
        String cityKey = city.toString();
        List<MessageEmbed> embeds = new ArrayList<>();
        EmbedBuilder template = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setTitle(city.flag + " " + city.name  + " Market")
                .setDescription("Buy an item with the `/buy <item> [quantity]` command.\n"+"For more information on an item use the `/inspect <item>` command.");

        EmbedBuilder drugPage = new EmbedBuilder();
        drugPage.copyFrom(template);
        for (String key : bot.marketHandler.getCurrentDrugs(cityKey).keySet()) {
            Drugs drug = Drugs.valueOf(key);
            String field = String.format("%s __%s__ | %d %s", drug.emoji, drug.name, drug.price, CURRENCY);
            drugPage.addField(field, "A drug that you can consume.", false);
        }
        embeds.add(drugPage.build());

        EmbedBuilder materialsPage = new EmbedBuilder();;
        materialsPage.copyFrom(template);
        for (String key : bot.marketHandler.getCurrentMaterials(cityKey).keySet()) {
            Materials item = Materials.valueOf(key);
            String field = String.format("%s __%s__ | %d %s", item.emoji, item.name, item.price, CURRENCY);
            materialsPage.addField(field, "A material used for crafting.", false);
        }
        embeds.add(materialsPage.build());

        // Send embed
        ReplyCallbackAction action = event.replyEmbeds(embeds.get(0));
        if (embeds.size() > 1) {
            ButtonListener.sendPaginatedMenu(event.getUser().getId(), action, embeds);
        } else {
            action.queue();
        }
    }
}
