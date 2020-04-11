package ua.friends.telegram.bot.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminUtils {
    public static boolean isUserHasRights(Update update) {
        String login = update.getMessage().getFrom().getUserName();
        return login.equalsIgnoreCase(System.getenv("ADMIN"));
    }

    public static String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    public static String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }
}
