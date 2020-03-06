package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.entity.Sentence;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class PhraseDao {
    private Logger logger = Logger.getLogger(PhraseDao.class.getName());

    public void save(Phrase phrase) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(phrase);
            session.flush();
            tx.commit();
            logger.info(String.format("%s %s", phrase.toString(), "was saved"));
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
        } finally {
            session.close();
        }
    }

    public List<Phrase> getUsersPhrasesByUserIds(List<Integer> userIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from Phrase where authorTgId  in (:userIds)");
            query.setParameterList("userIds", userIds);
            tx = session.beginTransaction();
            List<Phrase> result = query.getResultList();
            session.flush();
            tx.commit();
            logger.info(String.format("%s %d %s", "was returned", result.size(), "rows"));
            return result;
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    public List<Phrase> getUsersPhrasesByPhraseIds(List<Integer> phraseIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from Phrase where phraseId  in (:phraseIds)");
            query.setParameterList("phraseIds", phraseIds);
            tx = session.beginTransaction();
            List<Phrase> result = query.getResultList();
            session.flush();
            tx.commit();
            logger.info(String.format("%s %d %s", "was returned", result.size(), "rows"));
            return result;
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback(
                );
            }
            logger.warning(e.getMessage());
            return Collections.emptyList();
        } finally {
            session.close();
        }
    }

    public void remove(List<Phrase> phrases) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Phrase phrase : phrases) {
                deleteSentencesInPhrase(session, phrase);
                session.delete(phrase);
                session.flush();
            }
            tx.commit();
            logger.info("Phrases removed");
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
        } finally {
            session.close();
        }
    }

    private void deleteSentencesInPhrase(Session session, Phrase phrase) {
        List<Sentence> sentences = phrase.getSentences();
        for (Sentence sentence : sentences) {
            sentence.setPhrase(null);
            session.delete(sentence);
        }
    }

}
