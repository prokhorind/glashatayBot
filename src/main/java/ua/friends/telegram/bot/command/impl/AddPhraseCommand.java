package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.entity.PhraseType;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.service.PhraseServiceImpl;
import ua.friends.telegram.bot.utils.AdminUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class AddPhraseCommand implements Command {

    @Inject
    private PhraseService phraseService;

    @Override
    public List<BotApiMethod> executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        int tgId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText().trim().split(" ", 2)[1];

        if (phraseService.count(Collections.singletonList(tgId)) >= 20 && !AdminUtils.isUserHasRights(update)) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Не больше 20 фраз от одного пользователя"));
        }


        boolean answer = text.length() < 2055;
        if (!answer) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Не больше 2055 символов в одном предложении"));
        }

        Optional<Phrase> optionalPhrase = phraseService.convert(tgId, text);
        if (optionalPhrase.isPresent()) {
            Phrase phrase = optionalPhrase.get();
            setPhraseType(phrase);
            phraseService.save(phrase);
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, "Сохранено"));
    }

    private void setPhraseType(Phrase phrase) {
        if (isDynamicPhrase(phrase)) {
            phrase.setPhraseType(PhraseType.DYNAMIC.name());
        } else {
            phrase.setPhraseType(PhraseType.COMMON.name());
        }
    }

    private boolean isDynamicPhrase(Phrase phrase) {
        return phrase.getSentence().contains("%gayname%");
    }
}
