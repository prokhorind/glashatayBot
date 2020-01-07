package ua.friends.telegram.bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.impl.DeleteMessageCommand;
import ua.friends.telegram.bot.command.impl.GlashatayCommand;
import ua.friends.telegram.bot.command.impl.InvalidMessageCommand;

import java.util.HashMap;
import java.util.Map;

public class TextCommandExecutor {

    private static Map<Endpoint, Command> commandMap = new HashMap();

    public TextCommandExecutor() {
        commandMap.put(Endpoint.SAY, new GlashatayCommand());
        commandMap.put(Endpoint.INVALID, new InvalidMessageCommand());
        commandMap.put(Endpoint.DELETE,new DeleteMessageCommand());
    }

    public BotApiMethod execute(Endpoint endpoint, Update update) {
            Command command = commandMap.get(endpoint);
            BotApiMethod message = command.executeCommand(update);
            return message;
    }
}
