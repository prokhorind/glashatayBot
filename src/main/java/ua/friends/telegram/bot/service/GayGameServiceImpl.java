package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.command.impl.BanCommand;
import ua.friends.telegram.bot.dao.ChatDao;
import ua.friends.telegram.bot.dao.GayGameDao;
import ua.friends.telegram.bot.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.inject.Inject;

public class GayGameServiceImpl implements GayGameService{

    private Logger logger = Logger.getLogger(BanCommand.class.getName());
    @Inject
    private GayGameDao gayGameDao;
    @Inject
    private ChatDao chatDao;
    @Inject
    private CronInfoService cronInfoService;


    public void reg(User user, Chat chat) {
        gayGameDao.regUser(user, chat);
    }

    public void remove(User user, Chat chat) {
        gayGameDao.removeUser(user, chat);
    }

    public Optional<GayGame> find(int userTgId, long chatId, int year) {
        return gayGameDao.find(userTgId, chatId, year);
    }

    public List<GayGame> find(long chatId, int year) {
        return gayGameDao.find(chatId, year);
    }

    public List<Object> find(long chatId) {
        return gayGameDao.find(chatId);
    }

    public void updateGameStats(Chat chat, User user, int count) {
        int currentYear = LocalDateTime.now().getYear();
        Optional<GayGame> optionalGayGame = find(user.getTgId(), chat.getChatId(), currentYear);
        GayGame gayGame = null;
        if (!optionalGayGame.isPresent()) {
            gayGame = createGayGameStatForUser(chat, user, currentYear);
        } else {
            gayGame = optionalGayGame.get();
        }
        logger.info("Gay game:" + gayGame.toString());
        int newCount = gayGame.getCount() + count;
        gayGame.setCount(newCount);
        gayGameDao.saveOrUpdate(gayGame);
    }

    public void updateFailedGameStat(Chat chat,int count) {
        int failedGames = chat.getNumberOfFailedGayChooseMessages();
        int newCount = failedGames+count;
        logger.warning("Gay game failed! in chat:" + chat.toString());
        chat.setNumberOfFailedGayChooseMessages(newCount);
        chatDao.saveOrUpdate(chat);
    }

    public void saveOrUpdate(GayGame gayGame) {
        gayGameDao.saveOrUpdate(gayGame);
    }

    public void merge(GayGame gayGame) {
        gayGameDao.merge(gayGame);
    }

    public void persist(GayGame gayGame) {
        gayGameDao.persisted(gayGame);
    }

    public void update(GayGame gayGame) {
        gayGameDao.update(gayGame);
    }

    public void save(GayGame gayGame) {
        gayGameDao.save(gayGame);
    }

    public Optional<CronInfo> getCronInfoForCurrentDay(Chat chat) {
        try {
            Optional<CronInfo> optionalCronInfo = cronInfoService.find(chat.getChatId(), Cron.GAY.getValue());
            if (optionalCronInfo.isPresent()) {
                CronInfo cronStat = optionalCronInfo.get();
                LocalDateTime lastUsed = cronStat.getLastUsage();
                LocalDateTime serverTime = LocalDateTime.now();
                if (isDaysEquals(lastUsed, serverTime)) {
                    logger.info("Job already used in:" + cronStat);
                    return optionalCronInfo;
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.warning(e.getMessage());
            return Optional.empty();
        }
    }

    private boolean isDaysEquals(LocalDateTime lastUsed, LocalDateTime serverTime) {

        return lastUsed.getYear() == serverTime.getYear() && lastUsed.getMonth().getValue() == serverTime.getMonth().getValue() && lastUsed.getDayOfWeek().getValue() == serverTime.getDayOfWeek().getValue();
    }

    private GayGame createGayGameStatForUser(Chat chat, User user, int currentYear) {
        GayGame gayGame = new GayGame();
        gayGame.setChat(chat);
        gayGame.setUser(user);
        gayGame.setYear(currentYear);
        return gayGame;
    }
}
