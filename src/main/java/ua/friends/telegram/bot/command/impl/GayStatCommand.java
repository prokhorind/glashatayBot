package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class GayStatCommand implements Command {
    public static final int OLD_VALUE_YEAR = 2019;
    private GayGameService gayGameService = new GayGameService();

    @Override
    public List<SendMessage> executeCommand(Update update) {
        String[] command = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder();
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
        List<GayGame> gayGameList = gayGameService.find(chatId, year);
        if (gayGameList.isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "В этом чате за год " + year + " нет результатов"));
        }

        createHeader(sb, year);
        gayGameList.forEach(g -> buildMessage(sb, g));
        return Collections.singletonList(MessageUtils.generateMessage(chatId, sb.toString()));
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


    private void buildMessage(StringBuilder sb, GayGame game) {
        sb.append(TelegramNameUtils.findName(game.getUser(), false));
        sb.append(":");
        sb.append(game.getCount());
        sb.append("\n");
    }
}
