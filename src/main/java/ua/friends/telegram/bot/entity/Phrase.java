package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Phrase implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PHRASE_ID")
    private int phraseId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "phrase")
    private List<Sentence> sentences = new ArrayList<>();

    @Column
    private int authorTgId;

    @Column
    private String phraseType;

    public Phrase() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(int phraseId) {
        this.phraseId = phraseId;
    }

    public int getAuthorTgId() {
        return authorTgId;
    }

    public void setAuthorTgId(int authorTgId) {
        this.authorTgId = authorTgId;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public String getPhraseType() {
        return phraseType;
    }

    public void setPhraseType(String phraseType) {
        this.phraseType = phraseType;
    }

    @Override
    public String toString() {
        return "Phrase{" +
                "phraseId=" + phraseId +
                ", sentences=" + sentences +
                "}";
    }
}
