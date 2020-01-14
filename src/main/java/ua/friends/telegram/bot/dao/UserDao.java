package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.data.UserData;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserChatPreferences;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDao {

    public Optional<User> find(int tgId, long chatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("Select u FROM User as u LEFT JOIN  u.chats c ON c.chatId = :chatId where u.tgId= :tgId ");
            query.setParameter("tgId", tgId);
            query.setParameter("chatId", chatId);
            User user = (User) query.getSingleResult();
            session.flush();
            tx.commit();
            session.close();
            return Optional.of(user);
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    public Optional<User> find(String login, long chatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("Select u FROM User as u JOIN  u.chats c ON c.chatId = :chatId where u.login= :login ");
            query.setParameter("login", login);
            query.setParameter("chatId", chatId);
            User user = (User) query.getSingleResult();
            session.flush();
            tx.commit();
            session.close();
            return Optional.of(user);
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            return Optional.empty();
        } finally {
            session.close();
        }
    }


    public void update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(user);
        session.flush();
        tx.commit();
        session.close();
    }

    public void save(UserData userData) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setLogin(userData.getLogin());
        user.setTgId(userData.getTgId());
        List<UserChatPreferences> userChatPreferencesList = new ArrayList<>();
        user.setUserChatPreferences(userChatPreferencesList);
        try {
            tx = session.beginTransaction();
            session.save(user);
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
