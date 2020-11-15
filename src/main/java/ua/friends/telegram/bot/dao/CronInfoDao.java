package ua.friends.telegram.bot.dao;

import java.util.Optional;

import ua.friends.telegram.bot.entity.CronInfo;

public interface CronInfoDao {
    Optional<CronInfo> find(long chatId, String cronName);
    void save(CronInfo cronInfo);
}
