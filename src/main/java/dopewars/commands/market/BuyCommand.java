package dopewars.commands.market;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.data.items.Item;
import dopewars.handlers.MarketHandler;
import dopewars.util.enums.Cities;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.ThreadLocalRandom;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.enums.Emojis.CURRENCY;
import static dopewars.util.enums.Emojis.FAIL;

/**
 * Command that purchases an item from the market.
 *
 * @author TechnoVision
 */
public class BuyCommand extends Command {

    public BuyCommand(DopeWars bot) {
        super(bot);
        this.name = "buy";
        this.description = "Purchase an item from the shop.";
        this.category = Category.MARKET;
        this.args.add(new OptionData(OptionType.STRING, "item", "The name of the item to purchase", true));
        this.args.add(new OptionData(OptionType.INTEGER, "quantity", "The amount to purchase").setMinValue(1).setMaxValue(Integer.MAX_VALUE));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Get player data
        String username = event.getUser().getName();
        Player player = bot.playerHandler.getPlayer(event.getUser().getIdLong());
        String city = player.getCity();
        long balance = player.getCash();

        // Get item data
        String itemName = event.getOption("item").getAsString();
        long quantity = event.getOption("quantity") == null ? 1 : event.getOption("quantity").getAsInt();

        if (bot.marketHandler.hasItemListed(city, itemName)) {
            // Attempt to purchase drug
            MarketHandler.Listing listing = bot.marketHandler.getListing(city, itemName);
            long price = (quantity * listing.price());
            if (balance >= price) {
                if (ThreadLocalRandom.current().nextDouble() <= 0.05 && bot.itemHandler.getDrug(itemName) != null) {
                    // 5% chance to be busted by police
                    event.replyEmbeds(bot.marketHandler.bustedBuying(player, username, price)).queue();
                    return;
                }
                bot.economyHandler.removeMoney(player, price);
                bot.itemHandler.addItem(player, itemName, quantity);
                Item item = listing.item();
                String formattedPrice = NUM_FORMAT.format(price) + " " + CURRENCY;
                event.reply("**" + username + "** purchased " + quantity + " " + item.getEmoji() + " " + item.getName() + " for " + formattedPrice).queue();
                return;
            }
        } else {
            // Item does not exist in current market cycle
            event.reply(FAIL + " That item can't be purchased in **" + Cities.valueOf(city).name + "**! See valid items with `/shop` or `/fly` to a new city.").setEphemeral(true).queue();
            return;
        }

        // Not enough cash
        String text = FAIL + " You don't have enough cash to buy that! You currently have **"+NUM_FORMAT.format(balance)+"** " + CURRENCY;
        event.reply(text).setEphemeral(true).queue();
    }
}
