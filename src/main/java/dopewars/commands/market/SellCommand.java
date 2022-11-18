package dopewars.commands.market;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.items.Drugs;
import dopewars.items.ItemTypes;
import dopewars.items.Materials;
import dopewars.util.Cities;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.Emojis.CURRENCY;
import static dopewars.util.Emojis.FAIL;

/**
 * Command that sells an item from a player's inventory.
 *
 * @author TechnoVision
 */
public class SellCommand extends Command {

    public SellCommand(DopeWars bot) {
        super(bot);
        this.name = "sell";
        this.description = "Sell an item from your inventory.";
        this.category = Category.MARKET;
        this.args.add(new OptionData(OptionType.STRING, "item", "The name of the item to sell", true));
        this.args.add(new OptionData(OptionType.INTEGER, "quantity", "The amount to sell").setMinValue(1).setMaxValue(Integer.MAX_VALUE));
    }

    public void execute(SlashCommandInteractionEvent event) {
        // Get player data
        String username = event.getUser().getName();
        Player player = bot.playerHandler.getPlayer(event.getUser().getIdLong());
        String city = player.getCity();
        long balance = player.getCash();

        // Get item data
        String itemName = event.getOption("item").getAsString().toUpperCase();
        long itemCount = bot.playerHandler.countItem(player, itemName);
        long quantity = event.getOption("quantity") == null ? itemCount : event.getOption("quantity").getAsInt();

        if (bot.marketHandler.hasDrug(city, itemName)) {
            // Attempt to purchase drug
            long price = (quantity * bot.marketHandler.getDrugPrice(city, itemName));
            if (itemCount >= quantity) {
                bot.economyHandler.addMoney(player, price);
                bot.playerHandler.removeItem(player.getUser_id(), itemName, quantity, ItemTypes.DRUGS);
                Drugs drug = Drugs.valueOf(itemName);
                String formattedPrice = NUM_FORMAT.format(price) + " " + CURRENCY;
                event.reply("**" + username + "** sold " + quantity + " " + drug.emoji + " " + drug.name + " for " + formattedPrice).queue();
                return;
            }
        }
        else if (bot.marketHandler.hasMaterial(city, itemName)) {
            // Attempt to purchase material
            long price = (quantity * bot.marketHandler.getMaterialPrice(city, itemName));
            if (itemCount >= quantity) {
                bot.economyHandler.addMoney(player, price);
                bot.playerHandler.removeItem(player.getUser_id(), itemName, quantity, ItemTypes.MATERIALS);
                Materials mat = Materials.valueOf(itemName);
                String formattedPrice = NUM_FORMAT.format(price) + " " + CURRENCY;
                event.reply("**" + username + "** sold " + quantity + " " + mat.emoji + " " + mat.name + " for " + formattedPrice).queue();
                return;
            }
        } else {
            // Item does not exist in current market cycle
            event.reply(FAIL + " That item can't be sold in **" + Cities.valueOf(city).name + "**! See valid items with `/shop` or `/fly` to a new city.").setEphemeral(true).queue();
            return;
        }

        // Not enough cash
        String text = FAIL + " You don't have enough of that item! Check your current items with `/inventory`.";
        event.reply(text).setEphemeral(true).queue();
    }
}
