package ua.friends.telegram.bot.dao;

import java.util.Optional;

import ua.friends.telegram.bot.data.UserData;
import ua.friends.telegram.bot.entity.User;

public interface UserDao {
    Optional<User> find(int tgId, long chatId);
    Optional<User> find(String login, long chatId);
    void update(User user);
    void save(UserData userData);
}
