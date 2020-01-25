package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Chat implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @Column(name = "chat_id", unique = true, nullable = false)
    private long chatId;

    @ManyToMany(mappedBy = "chats", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();


    @ManyToMany(mappedBy = "gayChats", fetch = FetchType.EAGER)
    private Set<User> gayUsers = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "chat")
    private List<UserChatPreferences> userChatPreferences;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "chat")
    private Set<GayGame> gayGames = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "chat")
    private Set<CronInfo> cronInfos = new HashSet<>();


    public Chat() {
    }

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

    public void setGayUsers(Set<User> gayUsers) {
        this.gayUsers = gayUsers;
    }

    public Set<User> getGayUsers() {
        return gayUsers;
    }

    public List<UserChatPreferences> getUserChatPreferences() {
        return userChatPreferences;
    }

    public void setUserChatPreferences(List<UserChatPreferences> userChatPreferences) {
        this.userChatPreferences = userChatPreferences;
    }

    public Set<GayGame> getGayGames() {
        return gayGames;
    }

    public void setGayGames(Set<GayGame> gayGames) {
        this.gayGames = gayGames;
    }

    public Set<CronInfo> getCronInfos() {
        return cronInfos;
    }

    public void setCronInfos(Set<CronInfo> cronInfos) {
        this.cronInfos = cronInfos;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                '}';
    }
}
