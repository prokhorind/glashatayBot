package ua.friends.telegram.bot.command.impl;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;

import java.util.Collections;
import java.util.List;

public class InvalidMessageCommand implements Command {
    private static final String INVALID_COMMAND = "Invalid command";

    @Override
    public List<SendMessage> executeCommand(Update update) {
        return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), INVALID_COMMAND));
    }
}
