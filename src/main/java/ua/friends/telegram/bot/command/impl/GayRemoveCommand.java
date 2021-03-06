package ua.friends.telegram.bot.command.impl;

import java.util.Collections;
import java.util.List;
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
import ua.friends.telegram.bot.service.GayGameServiceImpl;
import ua.friends.telegram.bot.service.UserService;

public class GayRemoveCommand  implements Command {
    private static final String MESSAGE = "и так не участвует";
    private static final String SUCCESS_MESSAGE = "успешно удалён";
    @Inject
    private UserService userService;
    @Inject
    private ChatService chatService;
    @Inject
    private GayGameService gayGameService;

    @Override
    public List<SendMessage> executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        int tgId = update.getMessage().getFrom().getId();
        User user = userService.find(tgId, chatId).get();
        Chat chat = chatService.find(chatId).get();

        if (!user.getGayChats().stream().map(a -> a.getChatId()).collect(Collectors.toSet()).contains(chatId)) {
            return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), String.format("%s %s", user.getLogin(), MESSAGE)));
        }
        gayGameService.remove(user, chat);
        return Collections.singletonList(MessageUtils.generateMessage(update.getMessage().getChatId(), String.format("%s %s", user.getLogin(), SUCCESS_MESSAGE)));
    }
}
