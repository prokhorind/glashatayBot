package ua.friends.telegram.bot.command;

public enum Endpoint {
    SAY("/SAY"), INVALID("/INVALID"), DELETE("/DELETE"), PIDORREG("/PIDORREG"), PIDORDEL("/PIDORDEL"), BAN("/BAN"), UNBAN("/UNBAN");

    private String value;

    Endpoint(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
