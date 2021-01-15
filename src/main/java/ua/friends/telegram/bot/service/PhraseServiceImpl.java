package ua.friends.telegram.bot.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import ua.friends.telegram.bot.dao.PhraseDao;
import ua.friends.telegram.bot.entity.Phrase;

public class PhraseServiceImpl implements PhraseService {

    @Inject
    private PhraseDao phraseDao;


    public Optional<Phrase> convert(int tgId, String text) {
        Phrase phrase = new Phrase();
        phrase.setAuthorTgId(tgId);
        phrase.setSentence(text);
        return Optional.of(phrase);
    }

    public void delete(Phrase phrase) {
        phraseDao.remove(Collections.singletonList(phrase));
    }

    public Phrase getById(int id) {
        List<Phrase> phrases = phraseDao.getUsersPhrasesByPhraseIds(Collections.singletonList(id));
        if (phrases == null || phrases.size() != 1) {
            return null;
        }
        return phrases.get(0);
    }

    public void save(Phrase phrase) {
        phraseDao.save(phrase);
    }

    public int count(List<Integer> tgId) {
        return getUserPhrases(tgId).size();
    }

    public List<Phrase> getUserPhrases(List<Integer> tgId) {
        return phraseDao.getUsersPhrasesByUserIds(tgId);
    }

    public Phrase getRandomPhrase(List<Integer> userIds) {
        return phraseDao.getRandomPhrase(userIds);
    }

    public Optional<Phrase> get(int userId, int phraseId) {
        return phraseDao.getPhrase(userId, phraseId);
    }
}
