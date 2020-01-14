package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.CronInfoDao;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.Cron;
import ua.friends.telegram.bot.entity.CronInfo;

import java.time.LocalDateTime;
import java.util.Optional;

public class CronInfoService {

    private CronInfoDao cronInfoDao = new CronInfoDao();

    public void updateCronInfo(Chat chat) {
        long chatId = chat.getChatId();
        Optional<CronInfo> oCronInfo = find(chatId, Cron.GAY.getValue());
        CronInfo cronInfo = null;
        if (!oCronInfo.isPresent()) {
            cronInfo = createCronInfo(chat);
        } else {
            cronInfo = oCronInfo.get();
            cronInfo.setLastUsage(LocalDateTime.now());
        }
        save(cronInfo);
    }

    private CronInfo createCronInfo(Chat chat) {
        CronInfo cronInfo = new CronInfo();
        cronInfo.setChat(chat);
        cronInfo.setCronName(Cron.GAY.getValue());
        cronInfo.setLastUsage(LocalDateTime.now());
        return cronInfo;
    }

    private Optional<CronInfo> find(long chatId, String cronName) {
        return cronInfoDao.find(chatId, cronName);
    }

    private void save(CronInfo cronInfo) {
        cronInfoDao.save(cronInfo);
    }
}
