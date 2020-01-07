package ua.friends.telegram.bot.command;

import java.util.ArrayList;
import java.util.List;

public class WordsCollection {
    private static List<String> words = new ArrayList<>();

    static {
        words.add("провозгласил");
        words.add("об`явил");
        words.add("возгласил");
        words.add("заявил");
        words.add("изрёк");
        words.add("возвестил");
        words.add("вещает");
        words.add("воскликнул");
        words.add("произнёс речь");
        words.add("предрёк");
        words.add("пророчит");
        words.add("оповещает");
        words.add("резюмирует");
        words.add("глаголит");
    }

    public static List<String> getWords() {
        return words;
    }

    public static String getRandomWord() {
        double index = Math.random() * (words.size() - 1);
        return words.get((int) index);
    }
}
