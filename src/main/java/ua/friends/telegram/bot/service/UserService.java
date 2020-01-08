package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.UserDao;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

import java.util.Optional;
import java.util.Set;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public UserService(UserDao UserDao) {
        this.userDao = UserDao;
    }

    public void save(String firstName, String lastName, String login) {
        userDao.save(firstName, lastName, login);
    }

    public boolean isUserExist(String login) {
        return find(login).isPresent();
    }

    public boolean isUserHasChat(String login, long chatId) {
        Optional<User> optUser = find(login);
        if (!optUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optUser.get();
        Set<Chat> chatSet = user.getChats();
        return chatSet.contains(chatId);
    }

    public Optional<User> find(String login) {
        return userDao.findByLogin(login);
    }
}
