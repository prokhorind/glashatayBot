package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.command.WordsCollection;

public class GlashatayCommand implements Command {
    @Override
    public SendMessage executeCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = buildMessage(update);
        return MessageUtils.generateMessage(chatId, text);
    }

    private String buildMessage(Update update) {
        String text = update.getMessage().getText().split(" ",2)[1];
        StringBuilder sb = new StringBuilder();
        sb.append(findName(update));
        sb.append(" ");
        sb.append(WordsCollection.getRandomWord());
        sb.append(" ");
        sb.append("что:");
        sb.append(text);
        return sb.toString();
    }

    private String findName(Update update) {
        User user = update.getMessage().getFrom();
        String userName = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if (userName != null && !userName.isEmpty()) {
            return userName;
        } else if (isFirstsnameOrLastnameNotEmpty(firstName, lastName)) {
            return String.format("%s %s", setEmptyIfNull(firstName), setEmptyIfNull(lastName));
        } else {
            return "Annonymous";
        }

    }

    private boolean isFirstsnameOrLastnameNotEmpty(String firstName, String lastName) {
        return (firstName != null && !firstName.isEmpty()) || (lastName != null && !lastName.isEmpty());
    }

    private String setEmptyIfNull(String firstName) {
        return firstName == null ? "" : firstName;
    }

}
