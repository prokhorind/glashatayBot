package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.UserService;
import ua.friends.telegram.bot.service.UserToChatService;

import java.util.Optional;
import java.util.logging.Logger;

public class BanCommand implements Command {

    private UserService userService = new UserService();
    private UserToChatService userToChatService = new UserToChatService();

    private Logger logger = Logger.getLogger(BanCommand.class.getName());

    @Override
    public SendMessage executeCommand(Update update) {

        logger.info("Start ban command");
        if (!isUserHasRights(update)) {
            return MessageUtils.generateMessage(update.getMessage().getChatId(), "Соси " + update.getMessage().getFrom().getUserName());
        }
        String[] vars = update.getMessage().getText().split(" ", 3);
        int id = Integer.valueOf(vars[1]);
        logger.info("banId:" + id);
        long chatId = update.getMessage().getChatId();
        logger.info("chatId:" + chatId);
        long minutes = Long.valueOf(vars[2]);
        logger.info("minutes:" + minutes);
        logger.info("Got all variables for ban");
        Optional<User> user = userService.find(id, chatId);
        if (!user.isPresent()) {
            return MessageUtils.generateMessage(update.getMessage().getChatId(), "Нет такого юзера");
        }
        User usr = null;
        try {
            usr = user.get();
        } catch (Exception e) {
            usr = null;
            logger.warning(e.getMessage());
        }
        logger.info("Get user in ban command:" + usr.getTgId());
        userToChatService.banUser(usr, chatId, minutes);
        return MessageUtils.generateMessage(update.getMessage().getChatId(), "Забанен");
    }

    private boolean isUserHasRights(Update update) {
        String login = update.getMessage().getFrom().getUserName();
        return "prokhorind".equalsIgnoreCase(login);
    }
}
