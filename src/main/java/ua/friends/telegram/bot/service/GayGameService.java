package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.command.impl.BanCommand;
import ua.friends.telegram.bot.dao.GayGameDao;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class GayGameService {

    private Logger logger = Logger.getLogger(BanCommand.class.getName());
    private GayGameDao gayGameDao = new GayGameDao();

    public void reg(User user, Chat chat) {
        gayGameDao.regUser(user, chat);
    }

    public void remove(User user, Chat chat) {
        gayGameDao.removeUser(user, chat);
    }

    public Optional<GayGame> find(int userTgId, long chatId, int year) {
        return gayGameDao.find(userTgId, chatId, year);
    }

    public List<GayGame> find(long chatId, int year) {
        return gayGameDao.find(chatId, year);
    }

    public void updateGameStats(Chat chat, User user) {
        int currentYear = LocalDateTime.now().getYear();
        Optional<GayGame> optionalGayGame = find(user.getTgId(), chat.getChatId(), currentYear);
        GayGame gayGame = null;
        if (!optionalGayGame.isPresent()) {
            gayGame = createGayGameStatForUser(chat, user, currentYear);
        } else {
            gayGame = optionalGayGame.get();
        }
        logger.info("Gay game:" + gayGame.toString());
        int newCount = gayGame.getCount() + 1;
        gayGame.setCount(newCount);
        gayGameDao.save(gayGame);
    }

    private GayGame createGayGameStatForUser(Chat chat, User user, int currentYear) {
        GayGame gayGame = new GayGame();
        gayGame.setChat(chat);
        gayGame.setUser(user);
        gayGame.setYear(currentYear);
        return gayGame;
    }
}
