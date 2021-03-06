package ua.friends.telegram.bot.data.telegram.adminresponse;

import java.util.List;

public class RootTelegramAPi {

    private boolean ok;

    private List<ResultTelegramApi> result;

    public RootTelegramAPi(){};

    public List<ResultTelegramApi> getResult() {
        return result;
    }

    public void setResult(List<ResultTelegramApi> result) {
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean getOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "RootTelegramAPi{" +
            "ok=" + ok +
            ", result=" + result +
            '}';
    }
}
