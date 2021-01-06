package ua.friends.telegram.bot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

    @Deprecated
    @Column(columnDefinition = "integer default 0")
    private int numberOfFailedGayChooseMessages;

    @Column(columnDefinition = "boolean default true")
    private boolean autoPlayerPickEnabled;

    @Column
    private LocalDateTime lastMessage;

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

    public void setNumberOfFailedGayChooseMessages(int numberOfFailedGayChooseMessages) {
        this.numberOfFailedGayChooseMessages = numberOfFailedGayChooseMessages;
    }

    public void setAutoPlayerPickEnabled(boolean autoPlayerPickEnabled) {
        this.autoPlayerPickEnabled = autoPlayerPickEnabled;
    }

    public boolean isAutoPlayerPickEnabled() {
        return autoPlayerPickEnabled;
    }

    public int getNumberOfFailedGayChooseMessages() {
        return numberOfFailedGayChooseMessages;
    }

    public void setLastMessage(LocalDateTime lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessage() {
        return lastMessage;
    }

    @Override
    public String toString() {
        return "Chat{" +
            "chatId=" + chatId +
            '}';
    }
}
