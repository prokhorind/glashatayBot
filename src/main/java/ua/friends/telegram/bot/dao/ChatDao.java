package ua.friends.telegram.bot.dao;

import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Chat;

public interface ChatDao {
    Optional<Chat> findChatById(long chatId);
    void saveOrUpdate(Chat chat);
    List<Chat> getAllChats();
    void save(long chatId);
}
