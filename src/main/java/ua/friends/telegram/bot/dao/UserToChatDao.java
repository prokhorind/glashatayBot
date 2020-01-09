package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.BanPreferences;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public void banUser(User user, long minutes) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            BanPreferences banPreferences = (BanPreferences) user.getUserPreferences();
            banPreferences.setHasBan(Boolean.TRUE);
            LocalDateTime banTime = LocalDateTime.now().plusMinutes(minutes);
            banPreferences.setToBan(banTime);
            session.saveOrUpdate(banPreferences);
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

    public void unBan(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            BanPreferences bp = (BanPreferences) user.getUserPreferences();
            bp.setToBan(null);
            bp.setHasBan(Boolean.FALSE);
            session.saveOrUpdate(bp);
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


}
