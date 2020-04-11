package ua.friends.telegram.bot.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.impl.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextCommandExecutor {

    private static Map<Endpoint, Command> commandMap = new HashMap();

    public TextCommandExecutor() {
        commandMap.put(Endpoint.SAY, new GlashatayCommand());
        commandMap.put(Endpoint.INVALID, new InvalidMessageCommand());
        commandMap.put(Endpoint.DELETE, new DeleteMessageCommand());
        commandMap.put(Endpoint.PIDORREG, new GayRegCommand());
        commandMap.put(Endpoint.PIDORDEL, new GayRemoveCommand());
        commandMap.put(Endpoint.BAN, new BanCommand());
        commandMap.put(Endpoint.PUNCH, new PunchCommand());
        commandMap.put(Endpoint.TGID, new UserIdCommand());
        commandMap.put(Endpoint.RATMSG, new RatMessageCommand());
        commandMap.put(Endpoint.STAT, new GayStatCommand());
        commandMap.put(Endpoint.GAYTODAY, new GayChooseCommand());
        commandMap.put(Endpoint.ADDPHRASE, new AddPhraseCommand());
        commandMap.put(Endpoint.SHOWPHRASES, new ShowUserPhrasesCommand());
        commandMap.put(Endpoint.REMPHRASE, new RemovePhraseCommand());
        commandMap.put(Endpoint.IMPORT, new ImportGayGameStatsCommand());
        commandMap.put(Endpoint.ALLSTAT, new FullGayGameStatsCommand());
    }

    public static Map<Endpoint, Command> getCommandMap() {
        return commandMap;
    }

    public List<BotApiMethod> execute(Endpoint endpoint, Update update) {
        Command command = commandMap.get(endpoint);
        List<BotApiMethod> messages = command.executeCommand(update);
        return messages;
    }
}
