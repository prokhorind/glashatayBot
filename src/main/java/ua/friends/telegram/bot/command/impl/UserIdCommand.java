package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.UserService;

import java.util.Optional;

public class UserIdCommand implements Command {

    public static final String SET_NAME = "Укажи имя";

    private UserService userService = new UserService();

    @Override
    public SendMessage executeCommand(Update update) {
        String[] params = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();

        if (params.length == 1 || params[1] == null || params[1].isEmpty()) {
            return MessageUtils.generateMessage(chatId, SET_NAME);
        }
        String to = params[1].replaceAll("@", "");
        return MessageUtils.generateMessage(chatId, createMessage(to, chatId));
    }

    private String createMessage(String to, long chatID) {
        Optional<User> oUser = userService.find(to, chatID);
        if (oUser.isPresent()) ;
        return oUser.get().getTgId().toString();
    }
}