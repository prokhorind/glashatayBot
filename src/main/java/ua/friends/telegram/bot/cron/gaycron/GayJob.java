package ua.friends.telegram.bot.cron.gaycron;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.friends.telegram.bot.GlashatayBotImpl;
import ua.friends.telegram.bot.command.impl.BanCommand;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.CronInfoService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.UserToChatServiceImpl;
import ua.friends.telegram.bot.utils.Pair;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@DisallowConcurrentExecution
public class GayJob implements Job {

    private Logger logger = Logger.getLogger(BanCommand.class.getName());

    private GlashatayBotImpl bot;
    private UserToChatServiceImpl userToChatServiceImpl = new UserToChatServiceImpl();
    private GayGameService gayGameService = new GayGameService();
    private CronInfoService cronInfoService = new CronInfoService();

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        bot = (GlashatayBotImpl) arg0.getJobDetail().getJobDataMap().get("bot");
        List<Chat> chatList = userToChatServiceImpl.getAll();
        List<Pair<Chat, User>> gaysList = chatList.stream().filter(this::isChatHasGayPlayers).map(GayGameService::chooseGay).collect(Collectors.toList());
        gaysList.forEach(this::updateGameChats);
    }

    private void updateGameChats(Pair<Chat, User> pair) {
        Chat chat = pair.first;
        User user = pair.second;
        gayGameService.setCronInfoService(cronInfoService);
        if (!gayGameService.getCronInfoForCurrentDay(chat).isPresent()) {
            try {
                bot.executeFromCron(chat.getChatId(), createMessage(user));
                gayGameService.updateGameStats(chat, user, 1);
                cronInfoService.updateCronInfo(chat, user);
            } catch (TelegramApiException e) {
                logger.warning("Looks like chat was removed");
                logger.warning(e.getMessage());
                gayGameService.updateFailedGameStat(chat,1);
            }
        }
    }

    private String createMessage(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("Пидор дня:");
        sb.append(TelegramNameUtils.findName(user, TRUE));
        return sb.toString();

    }

    private boolean isChatHasGayPlayers(Chat chat) {
        return chat.getGayUsers().size() > 1;
    }
}
