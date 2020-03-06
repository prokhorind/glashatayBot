package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.Sentence;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class SentenceDao {
    private Logger logger = Logger.getLogger(SentenceDao.class.getName());

    public void save(List<Sentence> sentences) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            sentences.forEach(sentence -> {
                session.save(sentence);
                session.flush();
            });
            tx.commit();
            logger.info(String.format("%s %s", sentences.size(), "sentences was saved"));
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
