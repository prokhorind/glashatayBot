package ua.friends.telegram.bot.dao;

import java.util.List;
import java.util.Optional;

import ua.friends.telegram.bot.entity.Phrase;

public interface PhraseDao {
    void save(Phrase phrase);
    List<Phrase> getUsersPhrasesByUserIds(List<Integer> userIds);
    Optional<Phrase> getPhrase(int userId, int phraseId);
    Phrase getRandomPhrase(List<Integer> userIds);
    List<Phrase> getUsersPhrasesByPhraseIds(List<Integer> phraseIds);
    void remove(List<Phrase> phrases);
}
