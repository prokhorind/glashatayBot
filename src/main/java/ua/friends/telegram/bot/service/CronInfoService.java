package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.CronInfoDao;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.Cron;
import ua.friends.telegram.bot.entity.CronInfo;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.lang.Boolean.FALSE;

public class CronInfoService {

    private CronInfoDao cronInfoDao = new CronInfoDao();

    public Optional<CronInfo> find(long chatId, String cronName) {
        return cronInfoDao.find(chatId, cronName);
    }

    public void updateCronInfo(Chat chat, User user) {
        long chatId = chat.getChatId();
        Optional<CronInfo> oCronInfo = find(chatId, Cron.GAY.getValue());
        CronInfo cronInfo = null;
        if (!oCronInfo.isPresent()) {
            cronInfo = createCronInfo(chat);
        } else {
            cronInfo = oCronInfo.get();
        }
        cronInfo.setLastUsage(LocalDateTime.now());
        cronInfo.setGayName(TelegramNameUtils.findName(user, FALSE));
        save(cronInfo);
    }

    private CronInfo createCronInfo(Chat chat) {
        CronInfo cronInfo = new CronInfo();
        cronInfo.setChat(chat);
        cronInfo.setCronName(Cron.GAY.getValue());
        return cronInfo;
    }

    private void save(CronInfo cronInfo) {
        cronInfoDao.save(cronInfo);
    }
}
