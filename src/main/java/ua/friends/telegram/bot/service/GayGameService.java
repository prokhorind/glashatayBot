package ua.friends.telegram.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.CronInfo;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.utils.Pair;

public interface GayGameService {

    public static Pair<Chat, User> chooseGay(Chat chat) {
        User gayUser = chooseGayUser(chat);
        Pair<Chat, User> pair = new Pair(chat, gayUser);
        return pair;
    }

    public static User chooseGayUser(Chat chat) {
        List<User> players = new ArrayList<>(chat.getGayUsers());
        int randomGayIndex = (int) (0 + Math.random() * (players.size() - 1));
        return players.get(randomGayIndex);
    }

    public void reg(User user, Chat chat);

    public void remove(User user, Chat chat);

    public Optional<GayGame> find(int userTgId, long chatId, int year);

    public List<GayGame> find(long chatId, int year);

    public List<Object> find(long chatId);

    void updateGameStats(Chat chat, User user, int count);

    void updateFailedGameStat(Chat chat, int count);

    void saveOrUpdate(GayGame gayGame);

    void merge(GayGame gayGame);

    void persist(GayGame gayGame);

    void update(GayGame gayGame);

    void save(GayGame gayGame);

    Optional<CronInfo> getCronInfoForCurrentDay(Chat chat);
}
