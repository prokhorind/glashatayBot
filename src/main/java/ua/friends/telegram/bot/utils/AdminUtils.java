package ua.friends.telegram.bot.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ua.friends.telegram.bot.data.telegram.adminresponse.ResultTelegramApi;
import ua.friends.telegram.bot.data.telegram.adminresponse.RootTelegramAPi;
import ua.friends.telegram.bot.data.telegram.adminresponse.UserTelegramApi;

public class AdminUtils {

    private static final Logger logger = Logger.getLogger(AdminUtils.class.getName());

    private static final String CORE_API_URL = "https://api.telegram.org/bot";

    private static OkHttpClient httpClient = new OkHttpClient();

    public static boolean isUserHasRights(Update update) {
        User user = update.getMessage().getFrom();
        String login = user.getUserName();
        Integer userId = user.getId();
        long chatId = update.getMessage().getChatId();
        return isUserHerokuAdmin(login) || isUserTelegramChatAdmin(userId, chatId);
    }

    private static boolean isUserHerokuAdmin(String login) {
        return login.equalsIgnoreCase(System.getenv("ADMIN"));
    }

    private static boolean isUserTelegramChatAdmin(Integer userId, long chatId) {
        String url = String.format("%s%s", "/getChatAdministrators?chat_id=", chatId);
        Request request = new Request.Builder()
            .url(buildRequest(url))
            .build();
        Optional<Response> optionalResponse = getResponse(request);
        if (!optionalResponse.isPresent()) {
            return false;
        }
        Response response = optionalResponse.get();
        if (response.code() == 400){
                return false;
        }
        Optional<RootTelegramAPi> optionalParsedResponse = parseResponse(response);
        logger.info("OptionalParsedResponse="+optionalParsedResponse.toString());
        boolean isTgAdmin = optionalParsedResponse
            .map(rootTelegramAPi -> rootTelegramAPi.getResult().stream().anyMatch(r -> isTelegramAdmin(r, userId))).orElse(false);
        return isTgAdmin;
    }

    private static boolean isTelegramAdmin(ResultTelegramApi result, Integer tgId) {
        UserTelegramApi user = result.getUser();
        return !user.isIs_bot() && tgId == user.getId() && Arrays.asList("administrator", "creator").contains(result.getStatus());
    }

    private static Optional<RootTelegramAPi> parseResponse(Response response) {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
           RootTelegramAPi telegramAPi = om.readValue(response.body().string(), RootTelegramAPi.class);
           logger.info("roottelegramapi:"+telegramAPi.toString());
            return Optional.of(telegramAPi);
        } catch (IOException e) {
            logger.warning("Can't parse the response from telegram API" + e.getMessage());
            return Optional.empty();
        } catch (Exception e){
            logger.warning(e.getMessage());
            return Optional.empty();
        }

    }

    private static Optional<Response> getResponse(Request request) {
        try (Response response = httpClient.newCall(request).execute()) {
            logger.info("Got response:"+response.body().string());
            return Optional.of(response);
        } catch (IOException e) {
            logger.warning("Can't get the response from telegram API" + e.getMessage());
            return Optional.empty();
        }
    }

    private static String buildRequest(String url) {
      String fullUrl =  String.format("%s%s%s", CORE_API_URL, getBotToken(), url);
      return fullUrl;
    }

    public static String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    public static String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }
}
