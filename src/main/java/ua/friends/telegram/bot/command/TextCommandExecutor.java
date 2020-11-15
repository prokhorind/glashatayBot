package ua.friends.telegram.bot.command;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TextCommandExecutor {
    List<BotApiMethod> execute(Endpoint endpoint, Update update);
}
