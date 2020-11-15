package ua.friends.telegram.bot.dao;

import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserChatPreferences;

public interface UserToChatDao {
    void addChatToUser(User user, Chat chat);
    void banUser(User user, Chat chat, long minutes);
    void unBan(User user, long chatId);
    void createChatPreferences(User u, Chat c);
    boolean isBanned(UserChatPreferences userChatPreferences, long chatId);
}
