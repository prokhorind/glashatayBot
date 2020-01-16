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
        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder();
        List<GayGame> gayGameList = gayGameService.find(chatId, LocalDateTime.now().getYear());
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
