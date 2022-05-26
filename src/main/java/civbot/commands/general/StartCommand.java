package civbot.commands.general;

import civbot.CivBot;
import civbot.commands.Command;
import civbot.data.pojos.Player;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Command that creates user profile in database and displays tutorial.
 *
 * @author TechnoVision
 */
public class StartCommand extends Command {

    public StartCommand(CivBot bot) {
        super(bot);
        this.name = "start";
        this.description = "Start your journey";
    }

    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if (bot.cache.getPlayer(event.getUser().getIdLong()) != null) {
            event.getHook().sendMessage("You have already started your journey...").queue();
            // TODO: Show tutorial guide
        } else {
            Player player = new Player(event.getUser().getIdLong());
            bot.cache.addPlayer(player);
            bot.databaseManager.players.insertOne(player);
            event.getHook().sendMessage("You embark on your journey!").queue();
            // TODO: Show tutorial guide
        }
    }
}
