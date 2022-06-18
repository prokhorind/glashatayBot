package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.*;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.CronInfoService;
import ua.friends.telegram.bot.service.CronInfoServiceImpl;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.service.PhraseServiceImpl;
import ua.friends.telegram.bot.utils.TelegramNameUtils;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import static java.lang.Boolean.TRUE;

public class GayChooseCommand implements Command {

    @Inject
    private GayGameService gayGameService;

    @Inject
    private ChatService chatService;

    @Inject
    private CronInfoService cronInfoService;

    @Inject
    private PhraseService phraseService;

    @Override
    public List<SendMessage> executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        Chat chat = chatService.find(chatId).get();
        Optional<CronInfo> optionalCronInfo = gayGameService.getCronInfoForCurrentDay(chat);
        if (!optionalCronInfo.isPresent()) {
            User user = GayGameService.chooseGayUser(chat);
            gayGameService.updateGameStats(chat, user, 1);
            cronInfoService.updateCronInfo(chat, user);
            return createMessages(user, chat);
        }
        return Collections
            .singletonList(MessageUtils.generateMessage(chatId, "Підора дня вже обрано:" + optionalCronInfo.get().getGayName()));
    }

    private List<SendMessage> createMessages(User user, Chat chat) {
        List<SendMessage> sendMessages = new ArrayList<>();
        Set<User> gayUsers = chat.getGayUsers();
        List<Integer> userIds = gayUsers.stream().map(User::getTgId).collect(Collectors.toList());
        String gayName = TelegramNameUtils.findName(user, TRUE);
        Phrase phrase = phraseService.getRandomPhrase(userIds, chat.isPublicPhrasesEnabled());
        if (Objects.isNull(phrase)) {
            generateCommonEndMessage(chat, sendMessages, gayName);
            return sendMessages;
        }
        boolean isPhraseDynamic = phrase.getPhraseType().equalsIgnoreCase(PhraseType.DYNAMIC.name());
        boolean isPhraseCommon = phrase.getPhraseType().equalsIgnoreCase(PhraseType.COMMON.name());
        for (String sentence : phrase.getSentence().split("&")) {
            generateSentence(chat, sendMessages, gayName, isPhraseDynamic, sentence);
        }
        if (isPhraseCommon) {
            generateCommonEndMessage(chat, sendMessages, gayName);
        }
        return sendMessages;
    }

    private void generateSentence(Chat chat, List<SendMessage> sendMessages, String gayName, boolean isPhraseDynamic, String snt) {
        StringBuilder sb = new StringBuilder();
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
        sb.append("Підор дня:");
        sb.append(gayName);
        sendMessages.add(MessageUtils.generateMessage(chat.getChatId(), sb.toString()));
    }
}
