package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.BanChatPreferences;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserChatPreferences;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserToChatDao {

    public void addChatToUser(User user, Chat chat) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            user.getChats().add(chat);
            session.saveOrUpdate(user);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void banUser(User user, long chatId, long minutes) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<UserChatPreferences> bpList = user.getUserChatPreferences();
            Optional<UserChatPreferences> up = bpList.stream().filter(bp -> isUserHasChat(bp, chatId)).findAny();
            if (up.isPresent()) {
                session.saveOrUpdate(ban(up.get(), minutes));
            }
            session.saveOrUpdate(user);
            session.flush();
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public void unBan(User user, long chatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<UserChatPreferences> bpList = user.getUserChatPreferences();
            Optional<UserChatPreferences> up = bpList.stream().filter(bp -> isBanned(bp, chatId)).findAny();
            if (up.isPresent()) {
                session.saveOrUpdate(unBan(up.get()));
            }
            session.saveOrUpdate(user);
            session.flush();
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    private UserChatPreferences unBan(UserChatPreferences userChatPreferences) {
        BanChatPreferences bp = (BanChatPreferences) userChatPreferences;
        bp.setToBan(null);
        bp.setHasBan(Boolean.FALSE);
        return bp;
    }

    private UserChatPreferences ban(UserChatPreferences userChatPreferences, long minutes) {
        BanChatPreferences bp = (BanChatPreferences) userChatPreferences;
        bp.setHasBan(Boolean.TRUE);
        LocalDateTime banTime = LocalDateTime.now().plusMinutes(minutes);
        bp.setToBan(banTime);
        return bp;
    }

    public void createChatPreferences(User u, Chat c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            UserChatPreferences banPreference = new BanChatPreferences();
            banPreference.setUser(u);
            banPreference.setChat(c);
            session.saveOrUpdate(banPreference);
            session.flush();
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public boolean isBanned(UserChatPreferences userChatPreferences, long chatId) {
        BanChatPreferences banChatPreferences = (BanChatPreferences) userChatPreferences;
        long actualChatId = banChatPreferences.getChat().getChatId();
        return banChatPreferences.isHasBan() && actualChatId == chatId;
    }

    private boolean isUserHasChat(UserChatPreferences userChatPreferences, long chatId) {
        BanChatPreferences banChatPreferences = (BanChatPreferences) userChatPreferences;
        long actualChatId = banChatPreferences.getChat().getChatId();
        return actualChatId == chatId;
    }

}
