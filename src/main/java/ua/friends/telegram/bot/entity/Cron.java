package ua.friends.telegram.bot.entity;

public enum Cron {

    GAY("GAY");

    private String value;

    Cron(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
