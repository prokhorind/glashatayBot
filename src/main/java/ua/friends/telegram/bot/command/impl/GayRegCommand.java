package ua.friends.telegram.bot.command.impl;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.UserService;

import static ua.friends.telegram.bot.utils.TelegramNameUtils.findName;

public class GayRegCommand implements Command {

    private static final String MESSAGE = "уже участвует в игре";

    private static final String SUCCESS_MESSAGE = "успешно зарегистрирован";

    private UserService userService = new UserService();

    @Inject
    private ChatService chatService;

    private GayGameService gayGameService = new GayGameService();

    private Logger logger = Logger.getLogger(GayRegCommand.class.getName());

    @Override
    public List<SendMessage> executeCommand(Update update) {
        logger.info("GayRegCommandStarted");
        long chatId = update.getMessage().getChatId();
        int tgId = update.getMessage().getFrom().getId();
        User user = userService.find(tgId, chatId).get();
        Chat chat = chatService.find(chatId).get();
        String userName = findName(user, true);

        if (user.getGayChats().stream().map(a -> a.getChatId()).collect(Collectors.toSet()).contains(chatId)) {
            return Collections
                .singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), String.format("%s %s", userName, MESSAGE)));
        }
        gayGameService.reg(user, chat);
        return Collections.singletonList(
            MessageUtils.generateMessage(update.getMessage().getChatId(), String.format("%s %s", userName, SUCCESS_MESSAGE)));
    }
}
