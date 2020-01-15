package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import javax.ws.rs.DefaultValue;
import java.io.Serializable;

@Table
@Entity
public class GayGame implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gay_game_id")
    private int gayGameId;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;
    @Column
    private int year;
    @Column
    @DefaultValue("0")
    private int count;

    public GayGame() {
    }

    public int getGayGameId() {
        return gayGameId;
    }

    public void setGayGameId(int gayGameId) {
        this.gayGameId = gayGameId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
