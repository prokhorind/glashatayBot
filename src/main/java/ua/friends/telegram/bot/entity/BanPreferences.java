package ua.friends.telegram.bot.entity;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GP")
public class BanPreferences extends UserPreferences {

    @Column
    @ColumnDefault("false")
    private boolean hasBan;

    public BanPreferences(){};

    public void setHasBan(boolean hasBan) {
        this.hasBan = hasBan;
    }

    public boolean isHasBan() {
        return hasBan;
    }
}
