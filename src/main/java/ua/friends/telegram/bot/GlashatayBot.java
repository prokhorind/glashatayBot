package ua.friends.telegram.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface GlashatayBot {
    void onUpdateReceived(Update update);
    void executeFromCron(long chatId, String message) throws TelegramApiException;
}
