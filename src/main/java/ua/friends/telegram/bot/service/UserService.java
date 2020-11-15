package ua.friends.telegram.bot.service;

import java.util.Optional;

import ua.friends.telegram.bot.data.UserData;
import ua.friends.telegram.bot.entity.User;

public interface UserService {
    void save(UserData userData);
    boolean isUserExist(int tgId, long chatId);
    boolean isUserHasChat(int tgId, long chatId);
    boolean isUserChangedLogin(int tgId, long chatId, String messageLogin);
    void updateUser(int tgId, long chatId, String messageLogin);
    Optional<User> find(int id, long chatId);
    Optional<User> find(String login, long chatId);

}
