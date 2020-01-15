package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_CHAT_PREFERENCES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "UCP_TYPE")
public abstract class UserChatPreferences implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_PREFERENCE_ID")
    private int userPreferencesId;

    @ManyToOne(cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne(cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    protected Chat chat;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
