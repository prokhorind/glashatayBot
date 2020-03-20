package ua.friends.telegram.bot.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import ua.friends.telegram.bot.entity.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HibernateUtil {
    public static final String LOGIN = "login";
    public static final String PASS = "pass";
    public static final String URL = "url";
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                Map map = parseUrl(System.getenv("DATABASE_URL"));
                Configuration configuration = new Configuration();
                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, map.get(URL));
                settings.put(Environment.USER, map.get(LOGIN));
                settings.put(Environment.PASS, map.get(PASS));
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL94Dialect");
                settings.put(Environment.SHOW_SQL, "false");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Chat.class);
                configuration.addAnnotatedClass(UserChatPreferences.class);
                configuration.addAnnotatedClass(BanChatPreferences.class);
                configuration.addAnnotatedClass(GayGame.class);
                configuration.addAnnotatedClass(CronInfo.class);
                configuration.addAnnotatedClass(Sentence.class);
                configuration.addAnnotatedClass(Phrase.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sessionFactory;
    }

    private static Map<String, String> parseUrl(String url) {
        Map<String, String> map = new HashMap<>();
        String[] prefixAndConnectionUrl = url.split("://");
        String noPrefixConnectionUrl = prefixAndConnectionUrl[1];
        String login = noPrefixConnectionUrl.split(":")[0];
        String pass = noPrefixConnectionUrl.split(":")[1].split("@")[0];
        String urlDb = String.format("%s%s", "jdbc:postgresql://", noPrefixConnectionUrl.split(":", 2)[1].split("@")[1]);
        map.put(LOGIN, login);
        map.put(PASS, pass);
        map.put(URL, urlDb);
        return map;
    }
}
