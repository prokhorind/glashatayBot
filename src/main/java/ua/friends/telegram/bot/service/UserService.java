package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.UserDao;
import ua.friends.telegram.bot.data.UserData;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public UserService(UserDao UserDao) {
        this.userDao = UserDao;
    }

    public void save(UserData userData) {
        userDao.save(userData);
    }

    public boolean isUserExist(int tgId, long chatId) {
        return find(tgId, chatId).isPresent();
    }

    public boolean isUserHasChat(int tgId, long chatId) {
        Optional<User> optUser = find(tgId, chatId);
        if (!optUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optUser.get();
        Set<Chat> chatSet = user.getChats();
        return chatSet.stream().map(a -> a.getChatId()).collect(Collectors.toSet()).contains(chatId);
    }

    public Optional<User> find(int id, long chatId) {
        return userDao.find(id, chatId);
    }
}
