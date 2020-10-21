package ua.friends.telegram.bot.data.telegram.adminresponse;

public class ResultTelegramApi {

    private UserTelegramApi user;

    private String status;

    private boolean can_be_edited;

    private boolean can_change_info;

    private boolean can_delete_messages;

    private boolean can_invite_users;

    private boolean can_restrict_members;

    private boolean can_pin_messages;

    private boolean can_promote_members;

    public UserTelegramApi getUser() {
        return user;
    }

    public void setUser(UserTelegramApi user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCan_be_edited() {
        return can_be_edited;
    }

    public void setCan_be_edited(boolean can_be_edited) {
        this.can_be_edited = can_be_edited;
    }

    public boolean isCan_change_info() {
        return can_change_info;
    }

    public void setCan_change_info(boolean can_change_info) {
        this.can_change_info = can_change_info;
    }

    public boolean isCan_delete_messages() {
        return can_delete_messages;
    }

    public void setCan_delete_messages(boolean can_delete_messages) {
        this.can_delete_messages = can_delete_messages;
    }

    public boolean isCan_invite_users() {
        return can_invite_users;
    }

    public void setCan_invite_users(boolean can_invite_users) {
        this.can_invite_users = can_invite_users;
    }

    public boolean isCan_restrict_members() {
        return can_restrict_members;
    }

    public void setCan_restrict_members(boolean can_restrict_members) {
        this.can_restrict_members = can_restrict_members;
    }

    public boolean isCan_pin_messages() {
        return can_pin_messages;
    }

    public void setCan_pin_messages(boolean can_pin_messages) {
        this.can_pin_messages = can_pin_messages;
    }

    public boolean isCan_promote_members() {
        return can_promote_members;
    }

    public void setCan_promote_members(boolean can_promote_members) {
        this.can_promote_members = can_promote_members;
    }

    @Override
    public String toString() {
        return "ResultTelegramApi{" +
            "user=" + user +
            ", status='" + status + '\'' +
            ", can_be_edited=" + can_be_edited +
            ", can_change_info=" + can_change_info +
            ", can_delete_messages=" + can_delete_messages +
            ", can_invite_users=" + can_invite_users +
            ", can_restrict_members=" + can_restrict_members +
            ", can_pin_messages=" + can_pin_messages +
            ", can_promote_members=" + can_promote_members +
            '}';
    }
}
