package ua.friends.telegram.bot.command.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.utils.AdminUtils;

public class AdminResponseCommand implements Command {

    public static final int RESPONSE_TO_SPECIFIC_CHAT = 2;

    @Override
    public List<BotApiMethod> executeCommand(Update update) {
        String text = update.getMessage().getText().trim().split(" ", 2)[1];
        String[] arguments = text.split("&");
        long fromChatId = update.getMessage().getChatId();
        User user = update.getMessage().getFrom();

        if (!AdminUtils.isUserHerokuAdmin(user.getUserName())) {
            return Collections.singletonList(MessageUtils.generateMessage(fromChatId, "Нет прав"));
        }
        List<BotApiMethod> messages = new ArrayList<BotApiMethod>();

        if (arguments.length == RESPONSE_TO_SPECIFIC_CHAT) {
            return generateMessageResponseForSpecificChat(arguments, messages);
        }
        return Collections.singletonList(MessageUtils.generateMessage(fromChatId, "Ответ не отправлен"));
    }

    @NotNull
    private List<BotApiMethod> generateMessageResponseForSpecificChat(String[] command, List<BotApiMethod> messages) {
        long chatId = Long.parseLong(command[0]);
        String text = command[1];
        messages.add(MessageUtils.generateMessage(chatId, "From admin:"));
        messages.add(MessageUtils.generateMessage(chatId, text));
        return messages;
    }
}
