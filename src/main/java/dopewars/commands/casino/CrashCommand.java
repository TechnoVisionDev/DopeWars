package dopewars.commands.casino;

import dopewars.DopeWars;
import dopewars.commands.Category;
import dopewars.commands.Command;
import dopewars.data.cache.Player;
import dopewars.listeners.ButtonListener;
import dopewars.util.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;
import java.util.concurrent.*;

import static dopewars.DopeWars.NUM_FORMAT;
import static dopewars.util.Emojis.CURRENCY;
import static dopewars.util.Emojis.FAIL;

/**
 * Command that plays the crash gambling game.
 *
 * @author TechnoVision
 */
public class CrashCommand extends Command {

    public static final HashMap<Long, CrashGame> games = new HashMap<>();
    private static final ScheduledExecutorService tasks = Executors.newScheduledThreadPool(10);

    public CrashCommand(DopeWars bot) {
        super(bot);
        this.name = "crash";
        this.description = "Bet against a multiplier that can crash at any moment.";
        this.category = Category.CASINO;
        this.args.add(new OptionData(OptionType.INTEGER, "bet", "The amount you want to wager", true).setMinValue(1));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        // Get command data
        User user = event.getUser();
        long bet = event.getOption("bet").getAsLong();
        if (games.containsKey(user.getIdLong())) {
            String text = FAIL + " You are currently playing a game of crash!";
            event.reply(text).setEphemeral(true).queue();
            return;
        }
        Player player = bot.playerHandler.getPlayer(user.getIdLong());

        // Charge player for bet
        long balance = player.getCash();
        if (balance < bet) {
            String currency = CURRENCY + " **" + balance + "**";
            String text = FAIL + " You don't have enough money for this bet. You currently have " + currency + " in cash.";
            event.reply(text).setEphemeral(true).queue();
            return;
        }
        bot.economyHandler.removeMoney(player, bet);

        // Check for auto fail
        if (ThreadLocalRandom.current().nextDouble() <= 0.03) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                    .setColor(EmbedColor.ERROR.color)
                    .setDescription("Your bet: " + CURRENCY + " " + NUM_FORMAT.format(bet))
                    .addField("Crashed At", "x1.00", true)
                    .addField("Loss", CURRENCY + " -" + NUM_FORMAT.format(bet), true);
            event.replyEmbeds(embed.build()).addActionRow(Button.primary("cashout", "Cashout").asDisabled()).queue();
            return;
        }

        // Setup crash game
        double multiplier = 0.01 + (0.99 / ThreadLocalRandom.current().nextDouble());
        if (multiplier > 30) multiplier = 30;
        double finalMultiplier = multiplier;
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(EmbedColor.DEFAULT.color)
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription("Your Bet: " + CURRENCY + " " + NUM_FORMAT.format(bet))
                .addField("Multiplier", "x1.00", true)
                .addField("Profit", CURRENCY + " " + NUM_FORMAT.format(bet), true);

        // Start crash game
        String uuid = user.getId() + ":" + UUID.randomUUID();
        Button button = Button.primary("crash:cashout:"+uuid+":"+bet, "Cashout");
        ButtonListener.buttons.put(uuid, List.of(button));
        event.replyEmbeds(embed.build()).addActionRow(button).queue(msg -> {
            ScheduledFuture task = tasks.scheduleAtFixedRate(() -> {
                CrashGame game = games.get(user.getIdLong());
                game.currMultiplier += 0.1;
                String multiplierString = "x"+String.format("%.2f", game.currMultiplier);
                EmbedBuilder embed2 = new EmbedBuilder().setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl());
                if (game.currMultiplier >= game.maxMultiplier) {
                    embed2.setColor(EmbedColor.ERROR.color);
                    embed2.setDescription("Your bet: " + CURRENCY + " " + NUM_FORMAT.format(bet));
                    embed2.addField("Crashed At", multiplierString, true);
                    embed2.addField("Loss", CURRENCY + " -" + NUM_FORMAT.format(bet), true);
                    games.remove(user.getIdLong()).task.cancel(true);
                    Button disabledButton = ButtonListener.buttons.remove(uuid).get(0).asDisabled();
                    msg.editOriginalEmbeds(embed2.build()).setActionRow(disabledButton).queue();
                } else {
                    int profit = (int) (((double)bet)*game.currMultiplier);
                    embed2.setColor(EmbedColor.DEFAULT.color);
                    embed2.setDescription("Your bet: " + CURRENCY + " " + NUM_FORMAT.format(bet));
                    embed2.addField("Multiplier", multiplierString, true);
                    embed2.addField("Profit", CURRENCY + " " +NUM_FORMAT.format(profit), true);
                    msg.editOriginalEmbeds(embed2.build()).queue();
                }
            }, 1500, 1500, TimeUnit.MILLISECONDS);

            games.put(user.getIdLong(), new CrashGame(task, 1.0, finalMultiplier, bet));
        });
    }

    /**
     * Stop the multiplier and cashout.
     *
     * @param bot an instance of the DopeWars bot.
     * @param user the user playing this game.
     * @return a result embed for the current game.
     */
    public static MessageEmbed cashout(DopeWars bot, User user) {
        // Cancel game
        CrashGame game = games.remove(user.getIdLong());
        game.task.cancel(true);

        // Award profit
        int profit = (int) (((double)game.bet)*game.currMultiplier);
        Player player = bot.playerHandler.getPlayer(user.getIdLong());
        bot.economyHandler.addMoney(player, profit);

        // Send result embed
        String multiplierString = "x"+String.format("%.2f", game.currMultiplier);
        return new EmbedBuilder()
                .setColor(EmbedColor.SUCCESS.color)
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription("Your bet: " + CURRENCY + " " + NUM_FORMAT.format(game.bet))
                .addField("Multiplier", multiplierString, true)
                .addField("Win", CURRENCY + " " + NUM_FORMAT.format(profit), true)
                .build();
    }

    /**
     * Represents a Crash game and stores game data.
     */
    public static class CrashGame {

        ScheduledFuture task;
        double currMultiplier;
        double maxMultiplier;
        long bet;

        /**
         * Stores Crash game data
         *
         * @param task the scheduled runnable for this game.
         * @param currMultiplier the current multiplier.
         * @param maxMultiplier the maximum multiplier before crashing.
         * @param bet the bet made for this game.
         */
        public CrashGame(ScheduledFuture task, double currMultiplier, double maxMultiplier, long bet) {
            this.task = task;
            this.currMultiplier = currMultiplier;
            this.maxMultiplier = maxMultiplier;
            this.bet = bet;
        }
    }
}
