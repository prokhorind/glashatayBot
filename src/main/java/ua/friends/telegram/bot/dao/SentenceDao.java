package ua.friends.telegram.bot.dao;

import java.util.List;

import ua.friends.telegram.bot.entity.Sentence;

public interface SentenceDao {
    void save(List<Sentence> sentences);
}
