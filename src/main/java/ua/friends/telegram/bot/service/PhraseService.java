package ua.friends.telegram.bot.service;

import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Phrase;

public interface PhraseService {
    Optional<Phrase> convert(String[] sentences, int tgId);
    void delete(Phrase phrase);
    Phrase getById(int id);
    void save(Phrase phrase);
    int count(List<Integer> tgId);
    List<Phrase> getUserPhrases(List<Integer> tgId);
    Phrase getRandomPhrase(List<Integer> userIds);
    Optional<Phrase> get(int userId, int phraseId);
}
