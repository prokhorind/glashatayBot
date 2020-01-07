package ua.friends.telegram.bot.command;

public enum Endpoint {
    SAY("/SAY"),INVALID("/INVALID"),DELETE("/DELETE");

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
