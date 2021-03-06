package ua.friends.telegram.bot.command.impl;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.utils.AdminUtils;

public class FAQCommand implements Command {

    @Override
    public List<BotApiMethod> executeCommand(Update update) {
        String[] command = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();
        if (command.length != 2) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/main/resources/faq.txt");
                String data = IOUtils.toString(fis, StandardCharsets.UTF_8);
                return Collections.singletonList(MessageUtils.generateMessage(chatId, data));
            } catch (Exception e) {
                return Collections.singletonList(MessageUtils.generateMessage(chatId, "нет доступа к FAQ файлу"));
            }
        }
        List<BotApiMethod> messages = new ArrayList<BotApiMethod>();
        messages.add(MessageUtils.generateMessage(AdminUtils.getChatAdminId(), String.format("%s:%s:%s", update.getMessage().getFrom().toString(), chatId, command[1])));
        messages.add(MessageUtils.generateMessage(chatId, "Доставлено"));
        return messages;
    }

}
