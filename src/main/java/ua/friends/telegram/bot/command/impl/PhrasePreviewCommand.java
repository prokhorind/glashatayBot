package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.entity.PhraseType;
import ua.friends.telegram.bot.entity.Sentence;
import ua.friends.telegram.bot.exception.PhraseNotFoundException;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.service.PhraseServiceImpl;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class PhrasePreviewCommand implements Command {

    @Inject
    private PhraseService phraseService;

    @Override
    public List<SendMessage> executeCommand(Update update) {
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().split(" ", 2)[1];
        String user = TelegramNameUtils.findName(update.getMessage().getFrom(), false);
        try {
            int phraseId = Integer.parseInt(text);
            Optional<Phrase> phraseOptional = phraseService.get(userId, phraseId);
            Phrase phrase = phraseOptional.orElseThrow(PhraseNotFoundException::new);
            return getSendMessages(chatId, phrase);
        } catch (NumberFormatException e) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Введите id фразы"));
        } catch (PhraseNotFoundException e) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, String.format("%s %s %s", "У пользователя", user, "нет такой фразы")));
        }
    }

    private List<SendMessage> getSendMessages(long chatId, Phrase phrase) {
        List<SendMessage> sendMessages = new ArrayList<>();
        boolean isPhraseDynamic = phrase.getPhraseType().equalsIgnoreCase(PhraseType.DYNAMIC.name());
        StringBuilder sb = null;
        for (String snt : phrase.getSentence().split("&")) {
            sb = new StringBuilder();
            if (isPhraseDynamic) {
                sb.append(snt.replaceAll("%gayname%", "Джон Смит"));
            } else {
                sb.append(snt);
            }
            sb.append("\n");
            sendMessages.add(MessageUtils.generateMessage(chatId, sb.toString()));
        }
        return sendMessages;
    }
}
