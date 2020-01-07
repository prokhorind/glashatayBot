package ua.friends.telegram.bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@FunctionalInterface
public interface Command<T extends Serializable> {
    BotApiMethod<T> executeCommand(Update update);
}
