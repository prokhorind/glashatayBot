package ua.friends.telegram.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.friends.telegram.bot.command.Endpoint;
import ua.friends.telegram.bot.command.TextCommandExecutor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class GlashatayBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String command = message.getText().split(" ")[0];
        if (message.getFrom().getBot()) {
            return;
        }
        Endpoint endpoint = Stream.of(Endpoint.values()).filter(c -> c.getValue().equalsIgnoreCase(command)).findAny().orElse(Endpoint.INVALID);
        if (Objects.nonNull(endpoint)) {
            if (Endpoint.SAY.equals(endpoint)) {
                execute(update, Endpoint.DELETE);
                execute(update, endpoint);
            }
        }
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