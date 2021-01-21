package ua.friends.telegram.bot.command.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.service.ChatService;

public class PublicPhraseSwitcherCommand implements Command {

    @Inject
    ChatService chatService;

    @Override
    public List<BotApiMethod> executeCommand(Update update) {
        String[] command = update.getMessage().getText().split(" ", 2);
        long chatId = update.getMessage().getChatId();
        Chat chat = chatService.find(chatId).get();
        if (command.length != 2) {
            String response = chat.isPublicPhrasesEnabled() ? "Фразы из других чатов включены" : "Фразы из других чатов выключены";
            return Collections.singletonList(MessageUtils.generateMessage(chatId, response));
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, switchPublicPhrase(chat, command[1])));
    }

    private String switchPublicPhrase(Chat chat, String param) {
        if (!"ON".equalsIgnoreCase(param) && !"OFF".equalsIgnoreCase(param)) {
            return "Допустимое значение: ON/OFF ";
        }

        if ("ON".equalsIgnoreCase(param) && !chat.isAutoPlayerPickEnabled()) {
            chat.setPublicPhrasesEnabled(Boolean.TRUE);
            chatService.saveOrUpdate(chat);
            return "Фразы из других чатов включены";
        } else if ("OFF".equalsIgnoreCase(param) && chat.isAutoPlayerPickEnabled()) {
            chat.setPublicPhrasesEnabled(Boolean.FALSE);
            chatService.saveOrUpdate(chat);
            return "Фразы из других чатов выключены";
        } else {
            return "Попытка поменять на текущее значение";
        }
    }
}
