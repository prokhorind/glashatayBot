package ua.friends.telegram.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.friends.telegram.bot.command.Endpoint;
import ua.friends.telegram.bot.command.TextCommandExecutor;
import ua.friends.telegram.bot.command.impl.UnbanCommand;
import ua.friends.telegram.bot.data.MessageData;
import ua.friends.telegram.bot.service.UserToChatService;
import ua.friends.telegram.bot.utils.AdminUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class GlashatayBot extends TelegramLongPollingBot {

    private UserToChatService userToChatService = new UserToChatService();

    private Logger logger = Logger.getLogger(GlashatayBot.class.getName());

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
        logger.info("ENDPOINT:" + endpoint.getValue());
        processBan(update, messageData, endpoint);
        processNewData(message, messageData);
        processLoginChange(message, messageData);
        processCommands(update, endpoint);
    }

    private void processBan(Update update, MessageData messageData, Endpoint endpoint) {
        logger.config("Try to process BAN for user:" + messageData.getUserTgId());
        if (userToChatService.isBanned(messageData.getUserTgId(), messageData.getChatId()) && !isUserCanDoThisCommandWhileBanned(endpoint)) {
            logger.info("User banned:" + messageData.getUserTgId());
            if (!canUnBan(update)) {
                logger.info("Message from user deleted:" + messageData.getUserTgId());
                deleteMessage(update);
            }
        }
    }

    private void processNewData(Message message, MessageData messageData) {
        logger.config("Try to process new data:" + messageData.getUserTgId());
        if (!userToChatService.isUserHasChat(messageData.getUserTgId(), messageData.getChatId())) {
            userToChatService.processUserAndChatInDb(message);
        }
    }

    private void processLoginChange(Message message, MessageData messageData) {
        logger.config("Try to process login change:" + messageData.getUserTgId());
        if (userToChatService.isUserChangeLogin(messageData.getUserTgId(), messageData.getChatId(), message.getFrom().getUserName())) {
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
        List<SendMessage> msgs = new UnbanCommand().executeCommand(update);
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
        TextCommandExecutor textCommandExecutor = new TextCommandExecutor();
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
            e.printStackTrace();
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
