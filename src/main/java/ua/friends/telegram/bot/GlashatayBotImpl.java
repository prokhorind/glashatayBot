package ua.friends.telegram.bot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ua.friends.telegram.bot.command.Endpoint;
import ua.friends.telegram.bot.command.TextCommandExecutor;
import ua.friends.telegram.bot.command.impl.UnbanCommand;
import ua.friends.telegram.bot.config.GuiceDIConfig;
import ua.friends.telegram.bot.data.MessageData;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.UserToChatService;
import ua.friends.telegram.bot.utils.AdminUtils;

public class GlashatayBotImpl extends TelegramLongPollingBot implements GlashatayBot {

    @Inject
    private UserToChatService userToChatService;

    @Inject
    private ChatService chatService;

    @Inject
    private TextCommandExecutor textCommandExecutor;

    public GlashatayBotImpl() {
    };

    private Logger logger = Logger.getLogger(GlashatayBotImpl.class.getName());

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.getFrom().getBot()) {
            return;
        }
        MessageData messageData = getMessageData(message);
        processMessage(update, message, messageData);
    }

    public void executeFromCron(long chatId, String message) throws TelegramApiException {
        SendMessage sm = new SendMessage();
        sm.setText(message);
        sm.setChatId(chatId);
        execute(sm);
    }

    private void processMessage(Update update, Message message, MessageData messageData) {
        Endpoint endpoint = Stream.of(Endpoint.values()).filter(hasEndpoint(messageData.getCommand())).findAny().orElse(Endpoint.INVALID);
        logger.config("ENDPOINT:" + endpoint.getValue());
        processBan(update, messageData, endpoint);
        processNewData(message, messageData);
        processLastChatMessageDateUpdate(message, messageData);
        processLoginChange(message, messageData);
        processCommands(update, endpoint);
    }

    private void processLastChatMessageDateUpdate(Message message, MessageData messageData) {
        Chat chat = chatService.find(message.getChatId()).get();
        LocalDateTime oldDate = chat.getLastMessage();
        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(oldDate) || isDateShouldBeOverriden(oldDate, now)) {
            chat.setLastMessage(now);
            chatService.saveOrUpdate(chat);
        }
    }

    private boolean isDateShouldBeOverriden(LocalDateTime oldDate, LocalDateTime now) {
        return oldDate.getYear() < now.getYear() || (oldDate.getYear() == now.getYear() && oldDate.getMonthValue() < now.getMonthValue())
            || (oldDate.getYear() == now.getYear() && oldDate.getMonthValue() == now.getMonthValue()
                && oldDate.getDayOfMonth() < now.getDayOfMonth());
    }

    private void processBan(Update update, MessageData messageData, Endpoint endpoint) {
        if (userToChatService.isBanned(messageData.getUserTgId(), messageData.getChatId())
            && !isUserCanDoThisCommandWhileBanned(endpoint)) {
            logger.config("Try to process BAN for user:" + messageData.getUserTgId());
            logger.info("User banned:" + messageData.getUserTgId());
            if (!canUnBan(update)) {
                logger.info("Message from user deleted:" + messageData.getUserTgId());
                deleteMessage(update);
            }
        }
    }

    private void processNewData(Message message, MessageData messageData) {
        if (!userToChatService.isUserHasChat(messageData.getUserTgId(), messageData.getChatId())) {
            logger.config("Try to process new data:" + messageData.getUserTgId());
            userToChatService.processUserAndChatInDb(message);
        }
    }

    private void processLoginChange(Message message, MessageData messageData) {
        if (userToChatService.isUserChangeLogin(messageData.getUserTgId(), messageData.getChatId(), message.getFrom().getUserName())) {
            logger.config("Try to process login change:" + messageData.getUserTgId());
            userToChatService.updateUserLogin(messageData.getUserTgId(), messageData.getChatId(), message.getFrom().getUserName());
        }
    }

    private boolean isUserCanDoThisCommandWhileBanned(Endpoint endpoint) {
        return Endpoint.SAY.equals(endpoint) || Endpoint.RATMSG.equals(endpoint);
    }

    private MessageData getMessageData(Message message) {
        long chatId = message.getChatId();
        int tgId = message.getFrom().getId();
        String command = message.getText().split(" ")[0];
        command = command.replaceAll("@" + getBotUsername(), "");
        return new MessageData(chatId, tgId, command);
    }

    private Predicate<Endpoint> hasEndpoint(String command) {
        return c -> c.getValue().equalsIgnoreCase(command);
    }

    private boolean canUnBan(Update update) {
        Injector injector = Guice.createInjector(new GuiceDIConfig());
        List<SendMessage> msgs = injector.getInstance(UnbanCommand.class).executeCommand(update);
        return Objects.nonNull(msgs) && msgs.size() > 0;
    }

    private void deleteMessage(Update update) {
        execute(update, Endpoint.DELETE);
    }

    private void processCommands(Update update, Endpoint endpoint) {
        logger.config("Try to process endpoint:" + endpoint);
        if (Endpoint.INVALID.equals(endpoint)) {
            return;
        }
        if (Endpoint.SAY.equals(endpoint) || Endpoint.RATMSG.equals(endpoint)) {
            execute(update, Endpoint.DELETE);
        }
        execute(update, endpoint);
    }

    private void execute(Update update, Endpoint endpoint) {
        List<BotApiMethod> messages = textCommandExecutor.execute(endpoint, update);
        try {
            for (BotApiMethod message : messages) {
                execute(message);
                if (Endpoint.GAYTODAY.equals(endpoint)) {
                    Thread.sleep(2000);
                }
            }
        } catch (TelegramApiException e) {
            logger.warning(e.getMessage());
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
        }
    }

    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    public String getBotUsername() {
        return AdminUtils.getBotUsername();
    }

    public String getBotToken() {
        return AdminUtils.getBotToken();
    }
}
