package ua.friends.telegram.bot.utils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class TelegramNameUtils {

    public static String findName(Update update) {
        User user = update.getMessage().getFrom();
        return findName(user);
    }

    public static String findName(User user) {
        String userName = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if (userName != null && !userName.isEmpty()) {
            return String.format("%s%s", "@", userName);
        } else if (isFirstsnameOrLastnameNotEmpty(firstName, lastName)) {
            return String.format("%s %s", setEmptyIfNull(firstName), setEmptyIfNull(lastName));
        } else {
            return "Annonymous";
        }
    }

    public static String findName(ua.friends.telegram.bot.entity.User user) {
        String userName = user.getLogin();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if (userName != null && !userName.isEmpty()) {
            return String.format("%s%s", "@", userName);
        } else if (isFirstsnameOrLastnameNotEmpty(firstName, lastName)) {
            return String.format("%s %s", setEmptyIfNull(firstName), setEmptyIfNull(lastName));
        } else {
            return "Annonymous";
        }
    }

    private static boolean isFirstsnameOrLastnameNotEmpty(String firstName, String lastName) {
        return (firstName != null && !firstName.isEmpty()) || (lastName != null && !lastName.isEmpty());
    }

    private static String setEmptyIfNull(String firstName) {
        return firstName == null ? "" : firstName;
    }
}
