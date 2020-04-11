package ua.friends.telegram.bot.utils;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class TelegramNameUtils {

    public static String findName(Update update, boolean hasAtSign) {
        User user = update.getMessage().getFrom();
        return findName(user, hasAtSign);
    }

    public static String findName(User user, boolean hasAtSign) {
        String userName = user.getUserName();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        return getName(hasAtSign, userName, firstName, lastName);
    }

    public static String findName(String userName, String firstName, String lastName, boolean hasAtSign) {
        return getName(hasAtSign, userName, firstName, lastName);
    }


    public static String findName(ua.friends.telegram.bot.entity.User user, boolean hasAtSign) {
        String userName = user.getLogin();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        return getName(hasAtSign, userName, firstName, lastName);
    }

    private static String getName(boolean hasAtSign, String userName, String firstName, String lastName) {
        if (userName != null && !userName.isEmpty()) {
            if (hasAtSign) {
                return String.format("%s%s", "@", userName);
            } else {
                return userName;
            }
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
        return firstName == null || "NULL".equalsIgnoreCase(firstName) ? "" : firstName;
    }
}
