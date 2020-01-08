package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_PREFERENCES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "UP_TYPE")
public abstract class UserPreferences  implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_PREFERENCE_ID")
    private int userPreferencesId;

    @OneToOne(mappedBy = "userPreferences", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
