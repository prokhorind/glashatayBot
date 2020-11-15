package ua.friends.telegram.bot.dao;

import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.entity.User;

public interface GayGameDao {
    void regUser(User user, Chat chat);
    Optional<GayGame> find(int userTgId, long chatId, int year);
    List<GayGame> find(long chatId, int year);
    List<Object> find(long chatId);
    void saveOrUpdate(GayGame gayGame);
    void persisted(GayGame gayGame);
    void merge(GayGame gayGame);
    void save(GayGame gayGame);
    void update(GayGame gayGame);
    void removeUser(User user, Chat chat);
}
