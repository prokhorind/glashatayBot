package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.UserService;
import ua.friends.telegram.bot.service.UserToChatService;

import java.util.Optional;

public class BanCommand implements Command {

    private UserService userService = new UserService();
    private UserToChatService userToChatService = new UserToChatService();

    @Override
    public SendMessage executeCommand(Update update) {

        if (!isUserHasRights(update)) {
            return MessageUtils.generateMessage(update.getMessage().getChatId(), "Соси " + update.getMessage().getFrom().getUserName());
        }
        String[] vars = update.getMessage().getText().split(" ", 3);
        int id = Integer.valueOf(vars[1]);
        long chatId = update.getMessage().getChatId();
        long minutes = Long.valueOf(vars[2]);
        Optional<User> user = userService.find(id, chatId);
        if (!user.isPresent()) {
            return MessageUtils.generateMessage(update.getMessage().getChatId(), "Нет такого юзера");
        }

        User usr = user.get();
        userToChatService.banUser(usr.getTgId(), chatId, minutes);
        return MessageUtils.generateMessage(update.getMessage().getChatId(), "Забанен");
    }

    private boolean isUserHasRights(Update update) {
        String login = update.getMessage().getFrom().getUserName();
        return "prokhorind".equalsIgnoreCase(login);
    }
}
