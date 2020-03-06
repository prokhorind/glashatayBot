package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.Collections;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class PunchCommand implements Command {

    public static final String SET_NAME = "Укажи имя";

    @Override
    public List<SendMessage> executeCommand(Update update) {
        String from = TelegramNameUtils.findName(update, TRUE);
        String[] params = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();

        if (params.length == 1 || params[1] == null || params[1].isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, SET_NAME));
        }
        String to = params[1];
        return Collections.singletonList(MessageUtils.generateMessage(chatId, createMessage(from, to)));
    }

    private String createMessage(String from, String to) {
        StringBuilder sb = new StringBuilder();
        sb.append(from);
        sb.append("\t");
        sb.append("пнул");
        sb.append("\t");
        sb.append(to);
        return sb.toString();
    }
}
