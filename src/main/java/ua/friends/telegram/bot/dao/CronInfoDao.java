package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.CronInfo;

import javax.persistence.NoResultException;
import java.util.Objects;
import java.util.Optional;

public class CronInfoDao {

    public Optional<CronInfo> find(long chatId, String cronName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("SELECT c FROM CronInfo as c WHERE c.chat.chatId = :chatId AND c.cronName = :cronName");
            query.setParameter("chatId", chatId);
            query.setParameter("cronName", cronName);
            CronInfo gayGame = (CronInfo) query.getSingleResult();
            session.flush();
            tx.commit();
            session.close();
            return Optional.of(gayGame);
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    public void save(CronInfo cronInfo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.saveOrUpdate(cronInfo);
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
