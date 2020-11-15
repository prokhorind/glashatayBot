package ua.friends.telegram.bot.service;

import java.util.Optional;

import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.CronInfo;
import ua.friends.telegram.bot.entity.User;

public interface CronInfoService {
    Optional<CronInfo> find(long chatId, String cronName);
    void updateCronInfo(Chat chat, User user);
}
