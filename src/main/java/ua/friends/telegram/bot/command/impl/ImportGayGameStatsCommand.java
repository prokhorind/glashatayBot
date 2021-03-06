package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.GayGameServiceImpl;
import ua.friends.telegram.bot.service.UserService;
import ua.friends.telegram.bot.service.UserServiceImpl;
import ua.friends.telegram.bot.utils.AdminUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

public class ImportGayGameStatsCommand implements Command {

    @Inject
    private GayGameService gayGameService;
    @Inject
    private ChatService chatService;
    @Inject
    private UserService userService;

    @Override
    public List<SendMessage> executeCommand(Update update) {

        long chatId = update.getMessage().getChatId();
        if (!AdminUtils.isUserHasRights(update)) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Нет прав"));
        }
        String text = update.getMessage().getText().trim().split(" ", 2)[1];
        String[] arguments = text.split("&");
        if (arguments.length != 4) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Неправильное количество параметров"));
        }
        int tgId = Integer.parseInt(arguments[0]);
        int count = Integer.parseInt(arguments[1]);
        int year = Integer.parseInt(arguments[2]);
        long chatIdForGayGame = Long.parseLong((arguments[3]));
        Optional<User> optionalUser = userService.find(tgId, chatId);
        if (!optionalUser.isPresent()) {
            return Collections.singletonList(MessageUtils.generateMessage(chatId, "Пользователь с таким id не существует"));
        }

        String message = "Была создана новая запись";
        Optional<GayGame> optionalGayGame = gayGameService.find(tgId, chatIdForGayGame, year);
        GayGame gayGame = null;
        if (optionalGayGame.isPresent()) {
            gayGame = optionalGayGame.get();
            int oldCount = gayGame.getCount();
            int newCount = oldCount + count;
            gayGame.setCount(newCount);
            message = String.format("Стата обновлена с %d  на %d", oldCount, newCount);
            gayGameService.update(gayGame);
        } else {
            gayGame = new GayGame();
            gayGame.setCount(count);
            gayGame.setYear(year);
            gayGame.setUser(optionalUser.get());
            gayGame.setChat(chatService.find(chatIdForGayGame).get());
            gayGameService.save(gayGame);
        }


        return Collections.singletonList(MessageUtils.generateMessage(chatId, message));
    }
}
