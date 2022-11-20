package dopewars.commands.market;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.data.items.Item;
import dopewars.handlers.MarketHandler;
import dopewars.listeners.ButtonListener;
import dopewars.util.enums.Cities;
import dopewars.util.enums.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.ArrayList;
import java.util.List;

import static dopewars.util.enums.Emojis.CURRENCY;

/**
 * Command that displays the shop and available items.
 *
 * @author TechnoVision
 */
public class ShopCommand extends Command {

    public ShopCommand(DopeWars bot) {
        super(bot);
        this.name = "shop";
        this.description = "View items available to buy and sell.";
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
                .setDescription("You can `/fly` to another city to find different items.\nPrices change dynamically as players `/buy` and `/sell`.");

        // Build shop pages
        EmbedBuilder page = new EmbedBuilder();
        page.copyFrom(template);
        int index = 0;
        for (MarketHandler.Listing listing : bot.marketHandler.getListings(cityKey).values()) {
            Item item = listing.getItem();
            String field = String.format("%s __%s__ | %d %s", item.getEmoji(), item.getName(), listing.getPrice(), CURRENCY);
            page.addField(field, "A drug that you can consume.", false);
            index++;
            if (index % 6 == 0) {
                embeds.add(page.build());
                page.copyFrom(template);
            }
        }

        // Send shop embed as paginated menu
        ReplyCallbackAction action = event.replyEmbeds(embeds.get(0));
        if (embeds.size() > 1) {
            ButtonListener.sendPaginatedMenu(event.getUser().getId(), action, embeds);
        } else {
            action.queue();
        }
    }
}
