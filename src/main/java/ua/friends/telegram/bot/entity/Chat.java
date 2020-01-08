package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Chat {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @Column(name = "chat_id", unique = true, nullable = false)
    private long chatId;

    @ManyToMany(mappedBy = "chats", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Chat(){};

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<User> getUsers() {
        return users;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
