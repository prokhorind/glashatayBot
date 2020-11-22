package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.UserDao;
import ua.friends.telegram.bot.dao.UserDaoImpl;
import ua.friends.telegram.bot.data.UserData;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class UserServiceImpl implements UserService{
    @Inject
    private UserDao userDao;

    public UserServiceImpl(){};

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

    public boolean isUserChangedLogin(int tgId, long chatId, String messageLogin) {
        Optional<User> optUser = find(tgId, chatId);
        if (optUser.isPresent()) {
            if (optUser.get().getLogin() != messageLogin) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public void updateUser(int tgId, long chatId, String messageLogin) {
        Optional<User> optUser = find(tgId, chatId);
        if (optUser.isPresent()) {
            User u = optUser.get();
            u.setLogin(messageLogin);
            userDao.update(u);
        }
    }

    public Optional<User> find(int id, long chatId) {
        return userDao.find(id, chatId);
    }

    public Optional<User> find(String login, long chatId) {
        return userDao.find(login, chatId);
    }
}
