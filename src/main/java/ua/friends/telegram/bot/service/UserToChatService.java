package ua.friends.telegram.bot.service;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.Message;

import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

public interface UserToChatService {
    void banUser(User user, long chatId, long minutes);
    boolean isUserHasChat(int tgId, long chatId);
    boolean isBanned(int tgId, long chatId);
    void unBan(int tgID, long chatId);
    void processUserAndChatInDb(Message message);
    List<Chat> getAll();
    void updateUserLogin(int tgId, long chatId, String messageLogin);
    User getUser(int tgId, long chat);
    boolean isUserChangeLogin(int tgId, long chatId, String messageLogin);
    }
