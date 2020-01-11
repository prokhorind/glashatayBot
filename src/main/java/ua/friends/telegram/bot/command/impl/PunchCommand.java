package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

public class PunchCommand implements Command {

    public static final String SET_NAME = "Укажи имя";

    @Override
    public BotApiMethod executeCommand(Update update) {
        String from = TelegramNameUtils.findName(update);
        String[] params = update.getMessage().getText().split(" ");
        long chatId = update.getMessage().getChatId();

        if (params.length == 1 || params[1] == null || params[1].isEmpty()) {
            return MessageUtils.generateMessage(chatId, SET_NAME);
        }
        String to = params[1];
        return MessageUtils.generateMessage(chatId, createMessage(from, to));
    }

    private String createMessage(String from, String to) {
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(from);
        sb.append("\t");
        sb.append("пнул");
        sb.append("\t");
        sb.append(to);
        return sb.toString();
    }
}
