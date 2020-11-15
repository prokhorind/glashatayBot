package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.service.PhraseServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RemovePhraseCommand implements Command {

    @Inject
    private PhraseService phraseService;

    @Override
    public List<SendMessage> executeCommand(Update update) {
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        try {
            int phraseId = Integer.parseInt(update.getMessage().getText().split(" ")[1]);
            Phrase phrase = phraseService.getById(phraseId);
            if (Objects.isNull(phrase) || phrase.getAuthorTgId() != userId) {
                return Collections.singletonList(MessageUtils.generateMessage(chatId, "Пользователь не имеет предложения с таким id"));
            }
            phraseService.delete(phrase);
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Удалено"));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Введи номер фразы"));
        }
    }
}
