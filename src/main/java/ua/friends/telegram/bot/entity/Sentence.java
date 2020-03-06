package ua.friends.telegram.bot.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Sentence implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SENTENCE_ID")
    private int sentenceId;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "phrase_id")
    private Phrase phrase;

    @Column
    private String sentence;

    public Sentence() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return "{" + sentence + "}";
    }
}
