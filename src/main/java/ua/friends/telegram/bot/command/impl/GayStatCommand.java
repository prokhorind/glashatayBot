package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.time.LocalDateTime;
import java.util.List;

public class GayStatCommand implements Command {
    private GayGameService gayGameService = new GayGameService();

    @Override
    public SendMessage executeCommand(Update update) {
        String[] command = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder();
        int year;
        if (command.length == 2) {
            try {
                year = Integer.valueOf(command[1]);
            } catch (NumberFormatException e) {
                year = LocalDateTime.now().getYear();
            }
        } else {
            year = LocalDateTime.now().getYear();
        }
        List<GayGame> gayGameList = gayGameService.find(chatId, year);
        if (gayGameList.isEmpty()) {
          return MessageUtils.generateMessage(chatId, "В этом чате за год " + year + " нет результатов");
        }
        gayGameList.forEach(g -> buildMessage(sb, g));
        return MessageUtils.generateMessage(chatId, sb.toString());
    }

    private void buildMessage(StringBuilder sb, GayGame game) {
        sb.append(TelegramNameUtils.findName(game.getUser()));
        sb.append(":");
        sb.append(game.getCount());
        sb.append("\n");
    }
}
