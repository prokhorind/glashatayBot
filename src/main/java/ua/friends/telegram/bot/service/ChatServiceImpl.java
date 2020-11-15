package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.ChatDao;
import ua.friends.telegram.bot.dao.ChatDaoImpl;
import ua.friends.telegram.bot.entity.Chat;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class ChatServiceImpl implements ChatService{

    @Inject
    private ChatDao chatDao;

    public ChatServiceImpl() { }

    public void processChat(long chatId) {
        if (!isChatExist(chatId)) {
            save(chatId);
        }
    }

    public void save(long chatId) {
        chatDao.save(chatId);
    }

    public boolean isChatExist(long chatId) {
        return find(chatId).isPresent();
    }

    public Optional<Chat> find(long chatId) {
        return chatDao.findChatById(chatId);
    }

    public List<Chat> getAll() {
        return chatDao.getAllChats();
    }

}
