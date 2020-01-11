package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;

public class RatMessageCommand implements Command {
    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = generateMessage(update);
        return MessageUtils.generateMessage(chatId, text);
    }

    public String generateMessage(Update update) {
        StringBuilder stringBuilder = new StringBuilder();
        String text = update.getMessage().getText().split(" ", 2)[1];
        stringBuilder.append("Кто-то на крысу написал сообшение, в котором сказано:");
        stringBuilder.append(text);
        return stringBuilder.toString();
    }
}
