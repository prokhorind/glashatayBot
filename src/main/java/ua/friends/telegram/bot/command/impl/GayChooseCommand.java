package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.*;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.CronInfoService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

public class GayChooseCommand implements Command {

    private GayGameService gayGameService = new GayGameService();
    private ChatService chatService = new ChatService();
    private CronInfoService cronInfoService = new CronInfoService();
    private PhraseService phraseService = new PhraseService();

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
            return createMessages(user, chat);
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, "Пидор дня уже выбран:" + optionalCronInfo.get().getGayName()));
    }

    private List<SendMessage> createMessages(User user, Chat chat) {
        List<SendMessage> sendMessages = new ArrayList<>();
        Set<User> gayUsers = chat.getGayUsers();
        Phrase phrase = phraseService.getRandomPhrase(gayUsers.stream().map(User::getTgId).collect(Collectors.toList()));
        StringBuilder sb = null;
        for (Sentence sentence : phrase.getSentences()) {
            sb = new StringBuilder();
            sb.append(sentence.getSentence());
            sb.append("\n");
            sendMessages.add(MessageUtils.generateMessage(chat.getChatId(), sb.toString()));
        }
        sb = new StringBuilder();
        sb.append("Пидор дня:");
        sb.append(TelegramNameUtils.findName(user, TRUE));
        sendMessages.add(MessageUtils.generateMessage(chat.getChatId(), sb.toString()));
        return sendMessages;
    }
}
