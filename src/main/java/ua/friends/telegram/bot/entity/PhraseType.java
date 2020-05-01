package ua.friends.telegram.bot.entity;

public enum PhraseType {

    COMMON("COMMON"), DYNAMIC("DYNAMIC");

    private String value;

    PhraseType(String value) {
        this.value = value;
    }
}
