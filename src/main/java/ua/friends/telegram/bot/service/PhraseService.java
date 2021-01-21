package ua.friends.telegram.bot.service;

import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Phrase;

public interface PhraseService {
    Optional<Phrase> convert(int tgId, String text);
    void delete(Phrase phrase);
    Phrase getById(int id);
    void save(Phrase phrase);
    int count(List<Integer> tgId);
    List<Phrase> getUserPhrases(List<Integer> tgId);
    Phrase getRandomPhrase(List<Integer> userIds, boolean isPublicPhrasesEnabled);
    Optional<Phrase> get(int userId, int phraseId);
}
