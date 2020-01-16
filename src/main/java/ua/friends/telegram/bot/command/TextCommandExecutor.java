package ua.friends.telegram.bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.impl.*;

import java.util.HashMap;
import java.util.Map;

public class TextCommandExecutor {

    private static Map<Endpoint, Command> commandMap = new HashMap();

    public TextCommandExecutor() {
        commandMap.put(Endpoint.SAY, new GlashatayCommand());
        commandMap.put(Endpoint.INVALID, new InvalidMessageCommand());
        commandMap.put(Endpoint.DELETE,new DeleteMessageCommand());
        commandMap.put(Endpoint.PIDORREG, new GayRegCommand());
        commandMap.put(Endpoint.PIDORDEL, new GayRemoveCommand());
        commandMap.put(Endpoint.BAN, new BanCommand());
        commandMap.put(Endpoint.PUNCH, new PunchCommand());
        commandMap.put(Endpoint.TGID, new UserIdCommand());
        commandMap.put(Endpoint.RATMSG, new RatMessageCommand());
        commandMap.put(Endpoint.STAT, new GayStatCommand());
    }

    public BotApiMethod execute(Endpoint endpoint, Update update) {
            Command command = commandMap.get(endpoint);
            BotApiMethod message = command.executeCommand(update);
            return message;
    }
}
