package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserIdCommand implements Command {

    public static final String SET_NAME = "Укажи имя";
    public static final String SET_REAL_NAME = "Укажи реальное имя";

    private UserService userService = new UserService();

    @Override
    public List<SendMessage> executeCommand(Update update) {
        String[] params = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();

        if (params.length == 1 || params[1] == null || params[1].isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, SET_NAME));
        }
        String to = params[1].replaceAll("@", "");
        return Collections.singletonList(MessageUtils.generateMessage(chatId, createMessage(to, chatId)));
    }

    private String createMessage(String to, long chatID) {
        Optional<User> oUser = userService.find(to, chatID);
        if (oUser.isPresent()) {
            return oUser.get().getTgId().toString();
        } else {
            return SET_REAL_NAME;
        }
    }
}
