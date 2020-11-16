package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.GayGameServiceImpl;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

public class FullGayGameStatsCommand implements Command {

    @Inject
    private GayGameService gayGameService;

    @Override
    public List<SendMessage> executeCommand(Update update) {

        long chatId = update.getMessage().getChatId();
        StringBuilder sb = new StringBuilder();
        List<Object> gayGameList = gayGameService.find(chatId);
        if (gayGameList.isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "В этом чате в пидора не играют"));
        }
        addHeader(sb);
        AtomicInteger playerNum = new AtomicInteger(0);
        gayGameList.forEach(g -> buildMessage(sb, (Object[]) g, playerNum));
        return Collections.singletonList(MessageUtils.generateMessage(chatId, sb.toString(),"HTML"));
    }

    private void addHeader(StringBuilder sb) {
        sb.append("Результаты:");
        sb.append("\n");
    }

    private void buildMessage(StringBuilder sb, Object[] user, AtomicInteger playerNum) {
        long sum = (long) user[4];
        String login = (String) user[1];
        String name = (String) user[2];
        String surname = (String) user[3];
        sb.append(TelegramNameUtils.findName(login, name, surname, false));
        sb.append(String.format("%s%d%s%s","<b>",playerNum.incrementAndGet(),".","</b>"));
        sb.append(":");
        sb.append(sum);
        sb.append("\n");
    }
}
