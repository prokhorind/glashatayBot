package ua.friends.telegram.bot.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.MessageUtils;
import ua.friends.telegram.bot.entity.BanChatPreferences;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserChatPreferences;
import ua.friends.telegram.bot.service.UserToChatServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UnbanCommand implements Command {
    private UserToChatServiceImpl userToChatServiceImpl = new UserToChatServiceImpl();
    private Logger logger = Logger.getLogger(UnbanCommand.class.getName());

    @Override
    public List<SendMessage> executeCommand(Update update) {
        logger.info("Start unban");
        String login = update.getMessage().getFrom().getUserName();
        int tgId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();
        User user = userToChatServiceImpl.getUser(tgId, chatId);
        List<UserChatPreferences> bpList = user.getUserChatPreferences();
        Optional<UserChatPreferences> oBp = bpList.stream().filter(u -> u.getChat().getChatId() == chatId).findAny();
        if (oBp.isPresent()) {
            BanChatPreferences bp = (BanChatPreferences) oBp.get();
            if (bp.isHasBan()) {
                logger.info("Find user for unban:" + user.getTgId());
                LocalDateTime dateTime = LocalDateTime.now();
                // setting UTC as the timezone
                ZonedDateTime zonedUTC = dateTime.atZone(ZoneId.of("UTC"));

                logger.info("BanChatPreferences:" + bp.toString());
                LocalDateTime banTime = bp.getToBan();
                LocalDateTime currentZonedTine = zonedUTC.toLocalDateTime();
                logger.info("BANTIME:" + banTime);
                logger.info("DATETIMESERVE:" + currentZonedTine);
                logger.info("BANTIME COMPARE TO CURRZONETIME:" + banTime.compareTo(currentZonedTine));
                if (banTime.compareTo(currentZonedTine) <= 0) {
                    userToChatServiceImpl.unBan(tgId, chatId);
                    return Collections.singletonList(MessageUtils.generateMessage(chatId, login + " разбанен"));
                }
            }
        }
        return null;
    }
}
