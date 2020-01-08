package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.User;

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


}
