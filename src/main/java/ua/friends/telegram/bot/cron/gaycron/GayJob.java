package ua.friends.telegram.bot.cron.gaycron;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ua.friends.telegram.bot.GlashatayBot;
import ua.friends.telegram.bot.command.impl.BanCommand;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.CronInfoService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.UserToChatService;
import ua.friends.telegram.bot.utils.Pair;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@DisallowConcurrentExecution
public class GayJob implements Job {

    private Logger logger = Logger.getLogger(BanCommand.class.getName());

    private GlashatayBot bot;
    private UserToChatService userToChatService = new UserToChatService();
    private GayGameService gayGameService = new GayGameService();
    private CronInfoService cronInfoService = new CronInfoService();

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        bot = (GlashatayBot) arg0.getJobDetail().getJobDataMap().get("bot");
        List<Chat> chatList = userToChatService.getAll();
        List<Pair<Chat, User>> gaysList = chatList.stream().filter(this::isChatHasGayPlayers).map(this::chooseGay).collect(Collectors.toList());
        gaysList.forEach(this::updateGameChats);
    }

    private void updateGameChats(Pair<Chat, User> pair) {
        Chat chat = pair.first;
        User user = pair.second;
        try {
            gayGameService.updateGameStats(chat, user);
            bot.executeFromCron(chat.getChatId(), createMessage(user));
            cronInfoService.updateCronInfo(chat);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    private String createMessage(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("Пидор дня:");
        sb.append(TelegramNameUtils.findName(user));
        return sb.toString();

    }

    private boolean isChatHasGayPlayers(Chat chat) {
        return chat.getGayUsers().size() > 1;
    }

    private Pair<Chat, User> chooseGay(Chat chat) {
        List<User> players = new ArrayList<>(chat.getGayUsers());
        int randomGayIndex = (int) (0 + Math.random() * (players.size() - 1));
        User gayUser = players.get(randomGayIndex);
        Pair<Chat, User> pair = new Pair(chat, gayUser);
        return pair;
    }
}
