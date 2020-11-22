package ua.friends.telegram.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MessageUtils {

    public static SendMessage generateMessage(long chatId, String text) {
        return new SendMessage().setChatId(chatId).setText(text);
    }

    public static SendMessage generateMessage(long chatId, String text, String parseMode) {
        return new SendMessage().setChatId(chatId).setText(text).setParseMode(parseMode);
    }
}
