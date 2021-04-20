package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.GayGameServiceImpl;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.inject.Inject;

public class GayStatCommand implements Command {

    public static final int OLD_VALUE_YEAR = 2019;

    @Inject
    private GayGameService gayGameService;

    @Override
    public List<SendMessage> executeCommand(Update update) {
        Function<Integer,String> fullStatFooter = i -> String.format("%s%s:%s%s","<strong>", "Всего", i,"</strong>");
        String[] command = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder();
        int year = getYear(command);
        List<GayGame> gayGameList = gayGameService.find(chatId, year);
        if (gayGameList.isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "В этом чате за год " + year + " нет результатов"));
        }

        createHeader(sb, year);
        AtomicInteger playerNum = new AtomicInteger(0);
        AtomicInteger fullSumOfPick = new AtomicInteger(0);
        gayGameList.forEach(g -> buildMessage(sb, g, playerNum, fullSumOfPick));
        sb.append(fullStatFooter.apply(fullSumOfPick.get()));
        return Collections.singletonList(MessageUtils.generateMessage(chatId, sb.toString(), "HTML"));
    }

    private int getYear(String[] command) {
        int year;
        if (command.length == 2) {
            try {
                year = Integer.valueOf(command[1]);
                if (year < OLD_VALUE_YEAR) {
                    year = OLD_VALUE_YEAR;
                }
            } catch (NumberFormatException e) {
                year = LocalDateTime.now().getYear();
            }
        } else {
            year = LocalDateTime.now().getYear();
        }
        return year;
    }

    private void createHeader(StringBuilder sb, int year) {
        if (year <= OLD_VALUE_YEAR) {
            sb.append("Результаты до того как наебнулся старый пидор:");
            sb.append("\n");
        } else {
            sb.append("Результаты за ");
            sb.append(year);
            sb.append(" год");
            sb.append("\n");
        }
    }

    private void buildMessage(StringBuilder sb, GayGame game, AtomicInteger playerNum,AtomicInteger fullSumOfPick) {
        int count = game.getCount();
        sb.append(String.format("%s%d%s%s ", "<strong>", playerNum.incrementAndGet(), ".", "</strong>"));
        sb.append(TelegramNameUtils.findName(game.getUser(), false));
        sb.append(" : ");
        sb.append(count);
        sb.append("\n");
        fullSumOfPick.addAndGet(count);
    }
}
