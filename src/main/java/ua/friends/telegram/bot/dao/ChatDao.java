package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.Chat;

import javax.persistence.NoResultException;
import java.util.Objects;
import java.util.Optional;

public class ChatDao {

    public Optional<Chat> findChatById(long chatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from Chat where chatId = :chatId");
            query.setParameter("chatId", chatId);
            tx = session.beginTransaction();
            Chat chat = (Chat) query.getSingleResult();
            session.flush();
            tx.commit();
            return Optional.of(chat);
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            return Optional.empty();
        }finally {
            session.close();
        }
    }

    public void save(long chatId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Chat chat = new Chat();
        chat.setChatId(chatId);
        try {
            tx = session.beginTransaction();
            session.save(chat);
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
