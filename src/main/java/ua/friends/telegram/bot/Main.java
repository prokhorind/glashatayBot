package ua.friends.telegram.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import ua.friends.telegram.bot.config.GuiceDIConfig;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreator;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreatorImpl;

import java.util.TimeZone;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
        // Initialize Api Context
        ApiContextInitializer.init();
        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        // Register bot
        try {
            Injector injector = Guice.createInjector(new GuiceDIConfig());
            GlashatayBot glashatayBot = injector.getInstance(GlashatayBot.class);
            GayJobCreator gayJobCreator = injector.getInstance(GayJobCreator.class);
            botsApi.registerBot((LongPollingBot) glashatayBot);
            gayJobCreator.schedule();
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}