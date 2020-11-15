package ua.friends.telegram.bot.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import ua.friends.telegram.bot.GlashatayBot;
import ua.friends.telegram.bot.GlashatayBotImpl;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreator;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreatorImpl;
import ua.friends.telegram.bot.dao.ChatDao;
import ua.friends.telegram.bot.dao.ChatDaoImpl;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.ChatServiceImpl;
import ua.friends.telegram.bot.service.UserToChatService;
import ua.friends.telegram.bot.service.UserToChatServiceImpl;

public class GuiceDIConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(ChatDao.class).to(ChatDaoImpl.class).in(Scopes.SINGLETON);
        bind(ChatService.class).to(ChatServiceImpl.class).in(Scopes.SINGLETON);
        bind(UserToChatService.class).to(UserToChatServiceImpl.class).in(Scopes.SINGLETON);
        bind(GlashatayBot.class).to(GlashatayBotImpl.class).in(Scopes.SINGLETON);
        bind(GayJobCreator.class).to(GayJobCreatorImpl.class).in(Scopes.SINGLETON);
    }
}
