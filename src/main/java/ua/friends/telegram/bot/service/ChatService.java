package ua.friends.telegram.bot.service;

import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Chat;

public interface ChatService {
    void processChat(long chatId);
    void save(long chatId);
    boolean isChatExist(long chatId);
    Optional<Chat> find(long chatId);
    List<Chat> getAll();
}
