package ua.friends.telegram.bot.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TextCommandExecutorImpl implements TextCommandExecutor {

    @Inject
    @Named("glashatay")
    private Command glashatayCommand;

    @Inject
    @Named("invalidMessage")
    private Command invalidMessageCommand;

    @Inject
    @Named("deleteMessage")
    private Command deleteMessageCommand;

    @Inject
    @Named("gayReg")
    private Command gayRegCommand;

    @Inject
    @Named("ban")
    private Command banCommand;

    @Inject
    @Named("punch")
    private Command punchCommand;

    @Inject
    @Named("userId")
    private Command userIdCommand;

    @Inject
    @Named("ratMessage")
    private Command ratMessageCommand;

    @Inject
    @Named("gayStat")
    private Command gayStatCommand;

    @Inject
    @Named("gayChoose")
    private Command gayChooseCommand;

    @Inject
    @Named("addPhrase")
    private Command addPhraseCommand;

    @Inject
    @Named("showUserPhrases")
    private Command showUserPhasesCommand;

    @Inject
    @Named("removePhrases")
    private Command removePhrasesCommand;

    @Inject
    @Named("importGayGameStats")
    private Command importGayGameStatsCommand;

    @Inject
    @Named("fullGayGameStats")
    private Command fullGayGameStatsCommand;

    @Inject
    @Named("phrasePreview")
    private Command phrasePreviewCommand;

    @Inject
    @Named("gayRem")
    private Command gayRemCommand;

    @Inject
    @Named("autoPick")
    private Command autoPickCommand;

    @Inject
    @Named("publicPhrases")
    private Command publicPhraseSwitcherCommand;

    @Inject
    @Named("faq")
    private Command faqCommand;

    @Inject
    @Named("response")
    private Command responseCommand;

    private Map<Endpoint, Command> commandMap;

    @Inject
    private void init() {
        commandMap = new HashMap();
        commandMap.put(Endpoint.SAY, glashatayCommand);
        commandMap.put(Endpoint.INVALID, invalidMessageCommand);
        commandMap.put(Endpoint.DELETE, deleteMessageCommand);
        commandMap.put(Endpoint.PIDORREG, gayRegCommand);
        commandMap.put(Endpoint.PIDORDEL, gayRemCommand);
        commandMap.put(Endpoint.BAN, banCommand);
        commandMap.put(Endpoint.PUNCH, punchCommand);
        commandMap.put(Endpoint.TGID, userIdCommand);
        commandMap.put(Endpoint.RATMSG, ratMessageCommand);
        commandMap.put(Endpoint.STAT, gayStatCommand);
        commandMap.put(Endpoint.GAYTODAY, gayChooseCommand);
        commandMap.put(Endpoint.ADDPHRASE, addPhraseCommand);
        commandMap.put(Endpoint.SHOWPHRASES, showUserPhasesCommand);
        commandMap.put(Endpoint.REMPHRASE, removePhrasesCommand);
        commandMap.put(Endpoint.IMPORT, importGayGameStatsCommand);
        commandMap.put(Endpoint.ALLSTAT, fullGayGameStatsCommand);
        commandMap.put(Endpoint.PHRASEPREVIEW, phrasePreviewCommand);
        commandMap.put(Endpoint.AUTOPICK, autoPickCommand);
        commandMap.put(Endpoint.PUBLICPHRASES,publicPhraseSwitcherCommand);
        commandMap.put(Endpoint.FAQ,faqCommand);
        commandMap.put(Endpoint.RESPONSE,responseCommand);
    }

    public List<BotApiMethod> execute(Endpoint endpoint, Update update) {
        Command command = commandMap.get(endpoint);
        List<BotApiMethod> messages = command.executeCommand(update);
        return messages;
    }
}
