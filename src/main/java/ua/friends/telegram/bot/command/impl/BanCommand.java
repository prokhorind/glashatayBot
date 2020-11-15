package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.UserService;
import ua.friends.telegram.bot.service.UserToChatService;
import ua.friends.telegram.bot.utils.AdminUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.inject.Inject;

public class BanCommand implements Command {

    private UserService userService = new UserService();

    @Inject
    private UserToChatService userToChatService;

    private Logger logger = Logger.getLogger(BanCommand.class.getName());

    @Override
    public List<SendMessage> executeCommand(Update update) {

        logger.info("Start ban command");
        if (!AdminUtils.isUserHasRights(update)) {
            return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), "Соси " + update.getMessage().getFrom().getUserName()));
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
            return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), "Нет такого юзера"));
        }
        User usr = null;
        try {
            usr = user.get();
            if(usr.getLogin().equalsIgnoreCase(System.getenv("ADMIN"))){
                return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), "Nope"));
            }
        } catch (Exception e) {
            usr = null;
            logger.warning(e.getMessage());
        }
        logger.info("Get user in ban command:" + usr.getTgId());
        userToChatService.banUser(usr, chatId, minutes);
        return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), "Забанен"));
    }

}
