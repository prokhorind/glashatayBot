package ua.friends.telegram.bot.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Phrase implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PHRASE_ID")
    private int phraseId;

    @Column
    private int authorTgId;

    @Column
    private String phraseType;

    @Column(length = 2056)
    private String sentence;

    @Column(columnDefinition = "boolean default false")
    private boolean isPublic;

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

    public String getPhraseType() {
        return phraseType;
    }

    public void setPhraseType(String phraseType) {
        this.phraseType = phraseType;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "Phrase{" +
                "phraseId=" + phraseId +
                ", sentences=" + Arrays.toString(sentence.split("&")) +
                "}";
    }
}
