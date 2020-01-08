package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.BanPreferences;
import ua.friends.telegram.bot.entity.User;
import ua.friends.telegram.bot.entity.UserPreferences;

import javax.persistence.NoResultException;
import java.util.Objects;
import java.util.Optional;

public class UserDao {

    public Optional<User> findByLogin(String login) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from User where login = :login");
            query.setParameter("login", login);
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

    public void save(String firstName, String lastName, String login) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        UserPreferences gayPreference = new BanPreferences();
        gayPreference.setUser(user);
        try {
            tx = session.beginTransaction();
            session.save(user);
            session.save(gayPreference);
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
