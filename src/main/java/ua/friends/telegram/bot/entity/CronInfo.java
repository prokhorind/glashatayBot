package ua.friends.telegram.bot.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table
@Entity
public class CronInfo implements Serializable {
    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cron_info_id", unique = true, nullable = false)
    private long cronInfoId;
    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chat_id")
    private Chat chat;
    @Column
    private LocalDateTime lastUsage;
    @Column
    private String cronName;
    @Column
    private String gayName;

    public CronInfo() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getCronInfoId() {
        return cronInfoId;
    }

    public void setCronInfoId(long cronInfoId) {
        this.cronInfoId = cronInfoId;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public LocalDateTime getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(LocalDateTime lastUsage) {
        this.lastUsage = lastUsage;
    }

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName;
    }

    public String getGayName() {
        return gayName;
    }

    public void setGayName(String gayName) {
        this.gayName = gayName;
    }
}
