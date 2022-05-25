package civbot.commands.general;

import civbot.CivBot;
import civbot.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bson.Document;

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
        Document user = new Document("user_id", event.getUser().getId());
        if (bot.databaseManager.users.countDocuments(user) > 0) {
            event.getHook().sendMessage("You have already started your journey...").queue();
            // TODO: Show tutorial guide
        } else {
            bot.databaseManager.users.insertOne(user);
            event.getHook().sendMessage("You embark on your journey!").queue();
            // TODO: Show tutorial guide
        }
    }
}
