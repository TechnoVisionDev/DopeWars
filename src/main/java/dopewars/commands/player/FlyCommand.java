package dopewars.commands.player;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.data.items.Item;
import dopewars.util.enums.Cities;
import dopewars.util.enums.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static dopewars.util.enums.Emojis.CURRENCY;
import static dopewars.util.enums.Emojis.FAIL;

/**
 * Command that flies player to a new city.
 *
 * @author TechnoVision
 */
public class FlyCommand extends Command {

    public FlyCommand(DopeWars bot) {
        super(bot);
        this.name = "fly";
        this.description = "Fly to a new city.";
        this.category = Category.PLAYER;
        OptionData cityOption = new OptionData(OptionType.STRING, "city", "The name of the city to fly to");
        for (Cities city : Cities.values()) {
            cityOption.addChoice(city.name, city.toString().toUpperCase());
        }
        this.args.add(cityOption);
    }

    public void execute(SlashCommandInteractionEvent event) {
        // List ticket prices if city not specified
        OptionMapping cityOption = event.getOption("city");
        if (cityOption == null) {
            StringBuilder prices = new StringBuilder();
            for (Cities city : Cities.values()) {
                prices.append(city.flag).append(" ").append(city.name).append(" - ")
                        .append(city.price).append(" ").append(CURRENCY).append("\n");
            }
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(EmbedColor.DEFAULT.color)
                    .setTitle(":tickets: Ticket Prices")
                    .appendDescription("Use `/fly [city]` to purchase a ticket.\n\n")
                    .appendDescription(prices)
                    .build();
            event.replyEmbeds(embed).queue();
            return;
        }

        // Check if player is already at location
        Player player = bot.playerHandler.getPlayer(event.getUser().getIdLong());
        String cityKey = cityOption.getAsString();
        Cities city = Cities.valueOf(cityKey);
        if (player.getCity().equalsIgnoreCase(cityKey)) {
            event.reply(FAIL + " You are already at that location! You must pick a different city!").setEphemeral(true).queue();
            return;
        }

        // Check if player can afford ticket
        if (player.getCash() < city.price) {
            event.reply(FAIL + " You cannot afford that ticket price! You only have " + player.getCash() + " " + CURRENCY + " on you.").setEphemeral(true).queue();
            return;
        }

        // Fly to specified city
        bot.economyHandler.removeMoney(player, city.price);
        bot.playerHandler.fly(player, cityKey);
        MessageEmbed embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setTitle(city.flag + " Welcome to " + city.name)
                .setThumbnail(city.thumbnail)
                .appendDescription("**"+event.getUser().getName()+"** has arrived in " + city.name + " at " + city.getTime())
                .build();
        event.replyEmbeds(embed).queue();
    }
}
