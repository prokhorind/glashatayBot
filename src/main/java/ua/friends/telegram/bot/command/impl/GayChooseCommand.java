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
            gayGameService.updateGameStats(chat, user, 1);
            cronInfoService.updateCronInfo(chat, user);
            return createMessages(user, chat);
        }
        return Collections.singletonList(MessageUtils.generateMessage(chatId, "Пидор дня уже выбран:" + optionalCronInfo.get().getGayName()));
    }

    private List<SendMessage> createMessages(User user, Chat chat) {
        List<SendMessage> sendMessages = new ArrayList<>();
        Set<User> gayUsers = chat.getGayUsers();
        Phrase phrase = phraseService.getRandomPhrase(gayUsers.stream().map(User::getTgId).collect(Collectors.toList()));
        String gayName = TelegramNameUtils.findName(user, TRUE);
        boolean isPhraseDynamic = phrase.getPhraseType().equalsIgnoreCase(PhraseType.DYNAMIC.name());
        boolean isPhraseCommon = phrase.getPhraseType().equalsIgnoreCase(PhraseType.COMMON.name());
        for (Sentence sentence : phrase.getSentences()) {
            generateSentence(chat, sendMessages, gayName, isPhraseDynamic, sentence);
        }
        if (isPhraseCommon) {
            generateCommonEndMessage(chat, sendMessages, gayName);
        }
        return sendMessages;
    }

    private void generateSentence(Chat chat, List<SendMessage> sendMessages, String gayName, boolean isPhraseDynamic, Sentence sentence) {
        StringBuilder sb = new StringBuilder();
        String snt = sentence.getSentence();
        if (isPhraseDynamic) {
            sb.append(snt.replaceAll("%gayname%", gayName));
        } else {
            sb.append(snt);
        }
        sb.append("\n");
        sendMessages.add(MessageUtils.generateMessage(chat.getChatId(), sb.toString()));
    }

    private void generateCommonEndMessage(Chat chat, List<SendMessage> sendMessages, String gayName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Пидор дня:");
        sb.append(gayName);
        sendMessages.add(MessageUtils.generateMessage(chat.getChatId(), sb.toString()));
    }
}
