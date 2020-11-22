package ua.friends.telegram.bot.service;

import ua.friends.telegram.bot.dao.PhraseDao;
import ua.friends.telegram.bot.dao.PhraseDaoImpl;
import ua.friends.telegram.bot.dao.SentenceDao;
import ua.friends.telegram.bot.dao.SentenceDaoImpl;
import ua.friends.telegram.bot.entity.Phrase;
import ua.friends.telegram.bot.entity.Sentence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class PhraseServiceImpl  implements PhraseService{

    @Inject
    private PhraseDao phraseDao;
    @Inject
    private SentenceDao sentenceDao;

    public Optional<Phrase> convert(String[] sentences, int tgId) {
        if (sentences == null || sentences.length == 0) {
            return Optional.empty();
        }
        Phrase phrase = new Phrase();
        List<Sentence> sentenceList = Arrays.asList(sentences).stream().map(s -> create(s, phrase)).collect(Collectors.toList());
        phrase.setAuthorTgId(tgId);
        phrase.setSentences(sentenceList);
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
        List<Sentence> sentences = phrase.getSentences();
        phraseDao.save(phrase);
        sentenceDao.save(sentences);
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

    private Sentence create(String snt, Phrase phrase) {
        Sentence sentence = new Sentence();
        sentence.setPhrase(phrase);
        sentence.setSentence(snt);
        return sentence;
    }
}
