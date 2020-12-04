package ua.friends.telegram.bot.command.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.UserService;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

public class UserIdCommand implements Command {

    public static final String SET_NAME = "Укажи имя";
    public static final String SET_REAL_NAME = "Укажи реальное имя";

    @Inject
    private UserService userService;

    @Inject
    private ChatService chatService;

    @Override
    public List<SendMessage> executeCommand(Update update) {
        String[] params = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();

        if (params.length == 1 || params[1] == null || params[1].isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, SET_NAME));
        }

        if (params.length == 2 || "ALL".equalsIgnoreCase(params[1])) {
            return getAllUsersTgIdsInChat(chatId);
        }

        String to = params[1].replaceAll("@", "");
        return Collections.singletonList(MessageUtils.generateMessage(chatId, createMessage(to, chatId)));
    }

    @NotNull
    private List<SendMessage> getAllUsersTgIdsInChat(long chatId) {
        StringBuilder sb = new StringBuilder();
        Chat chat = chatService.find(chatId).get();
        AtomicInteger playerNum = new AtomicInteger(0);
        chat.getUsers().forEach(g -> buildMessage(sb, g, playerNum));
        return Collections.singletonList(MessageUtils.generateMessage(chatId, sb.toString(),"HTML"));
    }

    private void buildMessage(StringBuilder sb, User user, AtomicInteger playerNum) {
        sb.append(String.format("%s%d%s%s ","<strong>",playerNum.incrementAndGet(),".","</strong>"));
        sb.append(TelegramNameUtils.findName(user, false));
        sb.append(" : ");
        sb.append(user.getTgId());
        sb.append("\n");
    }

    private String createMessage(String login, long chatID) {
        Optional<User> oUser = userService.find(login, chatID);
        if (oUser.isPresent()) {
            return oUser.get().getTgId().toString();
        } else {
            return SET_REAL_NAME;
        }
    }
}
