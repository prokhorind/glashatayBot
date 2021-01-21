package ua.friends.telegram.bot.command;

public enum Endpoint {

    SAY("/SAY"), INVALID("/INVALID"), DELETE("/DELETE"), PIDORREG("/PIDORREG"), RATMSG("/RATMSG"),
    PIDORDEL("/PIDORDEL"), BAN("/BAN"), UNBAN("/UNBAN"), PUNCH("/PUNCH"),
    TGID("/TGID"), STAT("/STAT"), GAYTODAY("/GAYTODAY"), ADDPHRASE("/ADDPHRASE"),
    SHOWPHRASES("/SHOWPHRASES"), REMPHRASE("/REMPHRASE"), IMPORT("/IMPORTGAYSTAT"), PHRASEPREVIEW("/PHRASEPREVIEW"),
    ALLSTAT("/ALLSTAT"), AUTOPICK("/AUTOPICK") , PUBLICPHRASES("/PUBLICPHRASES");

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
