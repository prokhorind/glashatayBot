package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.Chat;
import ua.friends.telegram.bot.entity.GayGame;
import ua.friends.telegram.bot.entity.User;

import javax.persistence.NoResultException;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class GayGameDao {

    private Logger logger = Logger.getLogger(GayGameDao.class.getName());

    public void regUser(User user, Chat chat) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            user.getGayChats().add(chat);
            session.merge(user);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
        } finally {
            session.close();
        }
    }

    public Optional<GayGame> find(int userTgId, long chatId, int year) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("SELECT g FROM GayGame as g WHERE g.user.tgId = :userTgId AND g.chat.chatId = :chatId AND g.year = :year  ");
            query.setParameter("userTgId", userTgId);
            query.setParameter("chatId", chatId);
            query.setParameter("year", year);
            GayGame gayGame = (GayGame) query.getSingleResult();
            session.flush();
            tx.commit();
            return Optional.of(gayGame);
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    public void save(GayGame gayGame) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.saveOrUpdate(gayGame);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warning(e.getMessage());
        } finally {
            session.close();
        }
    }

    public void removeUser(User user, Chat chat) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            user.getGayChats().removeIf(e -> e.getChatId() == chat.getChatId());
            session.saveOrUpdate(user);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
        } finally {
            session.close();
        }
    }

}
