package ua.friends.telegram.bot.exception;

public class PhraseNotFoundException extends Exception {

    public PhraseNotFoundException() {
        super();
    }

    public PhraseNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
