package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.UserService;

import java.util.stream.Collectors;

public class GayRegCommand implements Command {
    private static final String MESSAGE = "уже зарегистрирован";
    private static final String SUCCESS_MESSAGE = "успешно зарегистрирован";
    private UserService userService = new UserService();
    private ChatService chatService = new ChatService();
    private GayGameService gayGameService = new GayGameService();

    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        int tgId = update.getMessage().getFrom().getId();
        User user = userService.find(tgId, chatId).get();
        Chat chat = chatService.find(chatId).get();

        if (user.getGayChats().stream().map(a -> a.getChatId()).collect(Collectors.toSet()).contains(chatId)) {
            return MessageUtils.generateMessage(update.getMessage().getChatId(), String.format("%s %s", user.getLogin(), MESSAGE));
        }
        gayGameService.reg(user, chat);
        return MessageUtils.generateMessage(update.getMessage().getChatId(), String.format("%s %s", user.getLogin(), SUCCESS_MESSAGE));
    }
}
