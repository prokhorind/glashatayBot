package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.GayGameDao;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

public class GayGameService {

    private GayGameDao gayGameDao = new GayGameDao();

    public void reg(User user, Chat chat) {
        gayGameDao.regUser(user, chat);
    }

    public void remove(User user, Chat chat) {
        gayGameDao.removeUser(user, chat);
    }
}
