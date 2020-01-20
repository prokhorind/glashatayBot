package ua.friends.telegram.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreator;

import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
        // Initialize Api Context
        ApiContextInitializer.init();
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        // Register bot
        try {
            GlashatayBot glashatayBot = new GlashatayBot();
            botsApi.registerBot(glashatayBot);
            GayJobCreator gayJobCreator = new GayJobCreator(glashatayBot);
            gayJobCreator.schedule();
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}