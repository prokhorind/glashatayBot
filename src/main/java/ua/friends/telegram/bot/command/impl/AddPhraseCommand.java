package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.service.PhraseService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AddPhraseCommand implements Command {

    private PhraseService phraseService = new PhraseService();

    @Override
    public List<BotApiMethod> executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        int tgId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText().trim().split(" ")[1];
        String[] sentences = text.split("&");

        if (phraseService.count(tgId) >= 5) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Не больше 5 фраз от одного пользователя"));
        }

        if (sentences.length < 1 || sentences.length > 6) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Не больше 5 предложений разделённых &"));
        }

        boolean answer = Arrays.stream(sentences).allMatch(s -> s.length() < 200);
        if (!answer) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Не больше 200 символов в одном предложении"));
        }

        Optional<Phrase> optionalPhrase = phraseService.convert(sentences, tgId);
        if (optionalPhrase.isPresent()) {
            phraseService.save(optionalPhrase.get());
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, "Сохранено"));
    }
}
