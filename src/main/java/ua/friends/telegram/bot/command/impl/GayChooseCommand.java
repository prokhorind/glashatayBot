package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.CronInfo;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.CronInfoService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

public class GayChooseCommand implements Command {

    private GayGameService gayGameService = new GayGameService();
    private ChatService chatService = new ChatService();
    private CronInfoService cronInfoService = new CronInfoService();

    @Override
    public List<SendMessage> executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        Chat chat = chatService.find(chatId).get();
        gayGameService.setCronInfoService(cronInfoService);
        Optional<CronInfo> optionalCronInfo = gayGameService.getCronInfoForCurrentDay(chat);
        if (!optionalCronInfo.isPresent()) {
            User user = GayGameService.chooseGayUser(chat);
            gayGameService.setCronInfoService(cronInfoService);
            gayGameService.updateGameStats(chat, user);
            cronInfoService.updateCronInfo(chat, user);
            return Collections.singletonList(MessageUtils.generateMessage(chatId, createMessage(user)));
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, "Пидор дня уже выбран:" + optionalCronInfo.get().getGayName()));
    }

    private String createMessage(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("Пидор дня:");
        sb.append(TelegramNameUtils.findName(user, TRUE));
        return sb.toString();
    }
}
