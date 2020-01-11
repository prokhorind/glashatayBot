package ua.friends.telegram.bot.data;

public class MessageData {
    private long chatId;
    private int userTgId;
    private String command;

    public MessageData(long chatId, int userTgId, String command) {
        this.chatId = chatId;
        this.userTgId = userTgId;
        this.command = command;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getUserTgId() {
        return userTgId;
    }

    public void setUserTgId(int userTgId) {
        this.userTgId = userTgId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
