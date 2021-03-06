package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;

import java.util.Collections;
import java.util.List;

public class DeleteMessageCommand implements Command {
    @Override
    public List<DeleteMessage> executeCommand(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(update.getMessage().getChatId());
        deleteMessage.setMessageId(update.getMessage().getMessageId());
        return Collections.singletonList(deleteMessage);
    }
}
