package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.command.WordsCollection;

import static ua.friends.telegram.bot.utils.TelegramNameUtils.findName;

public class GlashatayCommand implements Command {
    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = buildMessage(update);
        return MessageUtils.generateMessage(chatId, text);
    }

    private String buildMessage(Update update) {
        String text = update.getMessage().getText().split(" ",2)[1];
        StringBuilder sb = new StringBuilder();
        sb.append(findName(update));
        sb.append(" ");
        sb.append(WordsCollection.getRandomWord());
        sb.append(" ");
        sb.append("что:");
        sb.append(text);
        return sb.toString();
    }
}
