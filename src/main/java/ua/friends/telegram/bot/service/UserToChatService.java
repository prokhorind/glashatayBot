package ua.friends.telegram.bot.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ua.friends.telegram.bot.dao.UserToChatDao;
import ua.friends.telegram.bot.data.UserData;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserChatPreferences;

import java.util.List;
import java.util.Optional;

public class UserToChatService {
    private ChatService chatService = new ChatService();
    private UserService userService = new UserService();
    private UserToChatDao userToChatDao = new UserToChatDao();

    public void banUser(int tgId, long chatId, long minutes) {
        User u = userService.find(tgId, chatId).get();
        userToChatDao.banUser(u, chatId, minutes);
    }

    public boolean isUserHasChat(int tgId, long chatId) {
        Optional<User> user = userService.find(tgId, chatId);
        Optional<Chat> chat = chatService.find(chatId);
        if (user.isPresent() && chat.isPresent() && userService.isUserHasChat(tgId, chatId)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean isBanned(int tgId, long chatId) {
        Optional<User> u = userService.find(tgId, chatId);
        if (!u.isPresent()) {
            return Boolean.FALSE;
        }
        List<UserChatPreferences> userChatPreferencesList = u.get().getUserChatPreferences();
        Optional<UserChatPreferences> bannedPreference = userChatPreferencesList.stream().filter(ucp -> isBanned(ucp, chatId)).findAny();
        return bannedPreference.isPresent();
    }

    private boolean isBanned(UserChatPreferences userChatPreferences, long chatId) {
        return userToChatDao.isBanned(userChatPreferences, chatId);
    }

    public void unBan(int tgID, long chatId) {
        User u = userService.find(tgID, chatId).get();
        userToChatDao.unBan(u, chatId);
    }

    public void processUserAndChatInDb(Message message) {
        long chatId = message.getChatId();
        org.telegram.telegrambots.meta.api.objects.User from = message.getFrom();
        String login = from.getUserName();
        String firstName = from.getFirstName();
        String lastName = from.getLastName();
        int tgId = from.getId();
        UserData userData = new UserData(firstName, lastName, login, tgId);

        Optional<User> user = userService.find(tgId, chatId);
        Optional<Chat> chat = chatService.find(chatId);

        if (!user.isPresent() && !chat.isPresent()) {
            createUserAndChat(chatId, userData);
        } else if (user.isPresent() && !chat.isPresent()) {
            createChat(chatId, user);
        } else if (!user.isPresent() && chat.isPresent()) {
            createUser(userData, chat);
        } else if (user.isPresent() && chat.isPresent() && !userService.isUserHasChat(tgId, chatId)) {
            userToChatDao.addChatToUser(user.get(), chat.get());
        }
    }

    private void createUser(UserData userData, Optional<Chat> chat) {
        userService.save(userData);
        User user = userService.find(userData.getTgId(), chat.get().getChatId()).get();
        userToChatDao.addChatToUser(user, chat.get());
        userToChatDao.createChatPreferences(user, chat.get());
    }

    private void createChat(long chatId, Optional<User> user) {
        chatService.save(chatId);
        Chat chat = chatService.find(chatId).get();
        userToChatDao.addChatToUser(user.get(), chat);
        userToChatDao.createChatPreferences(user.get(), chat);
    }

    private void createUserAndChat(long chatId, UserData userData) {
        chatService.save(chatId);
        userService.save(userData);
        Chat createdChat = chatService.find(chatId).get();
        User createdUser = userService.find(userData.getTgId(), chatId).get();
        userToChatDao.addChatToUser(createdUser, createdChat);
        userToChatDao.createChatPreferences(createdUser, createdChat);
    }

    public boolean isUserChangeLogin(int tgId, long chatId, String messageLogin) {
        return userService.isUserChangedLogin(tgId, chatId, messageLogin);
    }

    public void updateUserLogin(int tgId, long chatId, String messageLogin) {
        userService.updateUser(tgId, chatId, messageLogin);
    }


    public User getUser(int tgId, long chat) {
        return userService.find(tgId, chat).get();
    }
}
