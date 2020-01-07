package ua.friends.telegram.bot.command.impl;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;

public class InvalidMessageCommand implements Command {
    private static final String INVALID_COMMAND = "Invalid command";
    @Override
    public SendMessage executeCommand(Update update) {
        return MessageUtils.generateMessage(update.getMessage().getChatId(),INVALID_COMMAND);
    }
}
