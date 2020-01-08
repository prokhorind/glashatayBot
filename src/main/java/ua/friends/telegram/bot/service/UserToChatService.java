package ua.friends.telegram.bot.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ua.friends.telegram.bot.dao.UserToChatDao;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

import java.util.Optional;

public class UserToChatService {
    private ChatService chatService = new ChatService();
    private UserService userService = new UserService();
    private UserToChatDao userToChatDao = new UserToChatDao();

    public void processUserAndChatInDb(Message message) {
        long chatId = message.getChatId();
        org.telegram.telegrambots.meta.api.objects.User from = message.getFrom();
        String login = from.getUserName();
        String firstName = from.getFirstName();
        String lastName = from.getLastName();

        Optional<User> user = userService.find(login);
        Optional<Chat> chat = chatService.find(chatId);

        if (!user.isPresent() && !chat.isPresent()) {
            createUserAndChat(chatId, login, firstName, lastName);
        } else if (user.isPresent() && !chat.isPresent()) {
            createChat(chatId, user);
        } else if (!user.isPresent() && chat.isPresent()) {
            createUser(login, firstName, lastName, chat);
        } else if( user.isPresent() && chat.isPresent() && !userService.isUserHasChat(login,chatId)){
            userToChatDao.addChatToUser(user.get(), chat.get());
        }
    }

    private void createUser(String login, String firstName, String lastName, Optional<Chat> chat) {
        userService.save(firstName, lastName, login);
        User user = userService.find(login).get();
        userToChatDao.addChatToUser(user, chat.get());
    }

    private void createChat(long chatId, Optional<User> user) {
        chatService.save(chatId);
        Chat chat = chatService.find(chatId).get();
        userToChatDao.addChatToUser(user.get(), chat);
    }

    private void createUserAndChat(long chatId, String login, String firstName, String lastName) {
        chatService.save(chatId);
        userService.save(firstName, lastName, login);
        Chat createdChat = chatService.find(chatId).get();
        User createdUser = userService.find(login).get();
        userToChatDao.addChatToUser(createdUser, createdChat);
    }
}
