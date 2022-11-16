package dopewars.commands.general;

import dopewars.DopeWars;
import dopewars.commands.Command;
import dopewars.data.pojos.Player;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command that creates user profile in database and displays tutorial.
 *
 * @author TechnoVision
 */
public class StartCommand extends Command {

    public StartCommand(DopeWars bot) {
        super(bot);
        this.name = "start";
        this.description = "Start your journey.";
    }

    public void execute(SlashCommandInteractionEvent event) {
        if (bot.playerHandler.getPlayer(event.getUser().getIdLong()) != null) {
            event.reply("You have already started your journey...").queue();
            // TODO: Show tutorial guide
        } else {
            Player player = new Player(event.getUser().getIdLong());
            bot.playerHandler.addPlayer(player);
            bot.databaseManager.players.insertOne(player);
            event.reply("You embark on your journey!").queue();
            // TODO: Show tutorial guide
        }
    }
}
