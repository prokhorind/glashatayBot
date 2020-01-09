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
import ua.friends.telegram.bot.service.UserToChatService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class GlashatayBot extends TelegramLongPollingBot {

    private UserToChatService userToChatService = new UserToChatService();

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.getFrom().getBot()) {
            return;
        }
        long chatId = message.getChatId();
        int tgId = message.getFrom().getId();
        String command = message.getText().split(" ")[0];
        Endpoint endpoint = Stream.of(Endpoint.values()).filter(c -> c.getValue().equalsIgnoreCase(command)).findAny().orElse(Endpoint.INVALID);

        if (userToChatService.isBanned(tgId, chatId) && !Endpoint.SAY.equals(endpoint)) {
            if (ban(update)) return;
        }

        userToChatService.processUserAndChatInDb(message);
        processCommands(update, endpoint);
    }

    private boolean ban(Update update) {
        SendMessage msg = new UnbanCommand().executeCommand(update);
        if (Objects.isNull(msg)) {
            deleteMessage(update);
            return true;
        }
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void deleteMessage(Update update) {
        execute(update, Endpoint.DELETE);
    }

    private void processCommands(Update update, Endpoint endpoint) {
        if (Endpoint.INVALID.equals(endpoint)) {
            return;
        }
        if (Endpoint.SAY.equals(endpoint)) {
            execute(update, Endpoint.DELETE);
        }
        execute(update, endpoint);
    }


    private void execute(Update update, Endpoint endpoint) {
        TextCommandExecutor textCommandExecutor = new TextCommandExecutor();
        BotApiMethod message = textCommandExecutor.execute(endpoint, update);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    public String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }

    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}
