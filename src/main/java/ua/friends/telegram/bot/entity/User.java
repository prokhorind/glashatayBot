package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;

    @Column
    private Integer tgId;

    @Column(name = "LOGIN", length = 100)
    private String login;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Chat",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "chat_id") }
    )
    Set<Chat> chats = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Gay_Chat",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "chat_id")}
    )
    Set<Chat> gayChats = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<UserChatPreferences> userChatPreferences;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "user")
    private Set<GayGame> gayGames = new HashSet<>();

    public User() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<UserChatPreferences> getUserChatPreferences() {
        return userChatPreferences;
    }

    public void setUserChatPreferences(List<UserChatPreferences> userChatPreferences) {
        this.userChatPreferences = userChatPreferences;
    }

    public void setGayChats(Set<Chat> gayChats) {
        this.gayChats = gayChats;
    }

    public Set<Chat> getGayChats() {
        return gayChats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public Integer getTgId() {
        return tgId;
    }

    public void setTgId(Integer tgId) {
        this.tgId = tgId;
    }

    public Set<GayGame> getGayGames() {
        return gayGames;
    }

    public void setGayGames(Set<GayGame> gayGames) {
        this.gayGames = gayGames;
    }
}
