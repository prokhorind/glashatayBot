package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.Collections;
import java.util.List;

public class ShowUserPhrasesCommand implements Command {

    private PhraseService phraseService = new PhraseService();

    @Override
    public List<SendMessage> executeCommand(Update update) {
        int userId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        List<Phrase> phrases = phraseService.getUserPhrases(userId);

        if (phrases == null || phrases.isEmpty()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, String.format("%s %s", TelegramNameUtils.findName(update, false), "не создал своих фраз")));
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, createMessage(phrases)));
    }

    private String createMessage(List<Phrase> phrases) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phrases.size(); i++) {
            createMessage(phrases.get(i), sb, i);
        }
        return sb.toString();
    }

    private void createMessage(Phrase phrase, StringBuilder sb, int number) {
        sb.append(number + 1);
        sb.append(". ");
        sb.append(phrase.toString());
        sb.append("\n");
    }
}
