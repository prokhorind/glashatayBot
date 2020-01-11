package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.BanChatPreferences;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserChatPreferences;
import ua.friends.telegram.bot.service.UserToChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UnbanCommand implements Command {
    private UserToChatService userToChatService = new UserToChatService();

    @Override
    public SendMessage executeCommand(Update update) {
        String login = update.getMessage().getFrom().getUserName();
        int tgId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        User user = userToChatService.getUser(tgId, chatId);
        List<UserChatPreferences> bpList = user.getUserChatPreferences();
        Optional<UserChatPreferences> oBp = bpList.stream().filter(u -> userToChatService.isBanned(tgId, chatId)).findAny();
        if (oBp.isPresent()) {
            BanChatPreferences bp = (BanChatPreferences) oBp.get();
            if (bp.getToBan().compareTo(LocalDateTime.now()) <= 0) {
                userToChatService.unBan(tgId, chatId);
                return MessageUtils.generateMessage(chatId, login + " разбанен");
            }
        }
        return null;
    }
}
