package ua.friends.telegram.bot.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.friends.telegram.bot.config.HibernateUtil;
import ua.friends.telegram.bot.entity.Phrase;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class PhraseDaoImpl implements PhraseDao{
    private Logger logger = Logger.getLogger(PhraseDaoImpl.class.getName());

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

    public Optional<Phrase> getPhrase(int userId, int phraseId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from Phrase where authorTgId = :userId and phraseId = :phraseId ");
            query.setParameter("userId", userId);
            query.setParameter("phraseId", phraseId);
            tx = session.beginTransaction();
            Phrase result = (Phrase) query.getSingleResult();
            session.flush();
            tx.commit();
            logger.config(String.format("%s %s", "was returned", result));
            return Optional.of(result);
        } catch (Exception e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    public Phrase getRandomPhrase(List<Integer> userIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from Phrase where authorTgId in (:userIds) order by rand()");
            query.setMaxResults(1);
            query.setParameterList("userIds", userIds);
            tx = session.beginTransaction();
            Phrase result = (Phrase) query.getSingleResult();
            session.flush();
            tx.commit();
            logger.info(String.format("%s %s", "was returned", result.toString()));
            return result;
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }

    public Phrase getRandomPhraseWithPublic(List<Integer> userIds) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Query query = session.createQuery("from Phrase where authorTgId in (:userIds) AND isPublic=:isPublic  order by rand()");
            query.setMaxResults(1);
            query.setParameterList("userIds", userIds);
            query.setParameter("isPublic", Boolean.TRUE);
            tx = session.beginTransaction();
            Phrase result = (Phrase) query.getSingleResult();
            session.flush();
            tx.commit();
            logger.info(String.format("%s %s", "was returned", result.toString()));
            return result;
        } catch (NoResultException e) {
            if (Objects.nonNull(tx)) {
                tx.rollback();
            }
            logger.warning(e.getMessage());
            return null;
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

}
