package ua.friends.telegram.bot.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

import ua.friends.telegram.bot.GlashatayBot;
import ua.friends.telegram.bot.GlashatayBotImpl;
import ua.friends.telegram.bot.command.Command;
import ua.friends.telegram.bot.command.TextCommandExecutor;
import ua.friends.telegram.bot.command.TextCommandExecutorImpl;
import ua.friends.telegram.bot.command.impl.AddPhraseCommand;
import ua.friends.telegram.bot.command.impl.AdminResponseCommand;
import ua.friends.telegram.bot.command.impl.AutoPickCommand;
import ua.friends.telegram.bot.command.impl.BanCommand;
import ua.friends.telegram.bot.command.impl.DeleteMessageCommand;
import ua.friends.telegram.bot.command.impl.FAQCommand;
import ua.friends.telegram.bot.command.impl.FullGayGameStatsCommand;
import ua.friends.telegram.bot.command.impl.GayChooseCommand;
import ua.friends.telegram.bot.command.impl.GayRegCommand;
import ua.friends.telegram.bot.command.impl.GayRemoveCommand;
import ua.friends.telegram.bot.command.impl.GayStatCommand;
import ua.friends.telegram.bot.command.impl.GlashatayCommand;
import ua.friends.telegram.bot.command.impl.ImportGayGameStatsCommand;
import ua.friends.telegram.bot.command.impl.InvalidMessageCommand;
import ua.friends.telegram.bot.command.impl.PhrasePreviewCommand;
import ua.friends.telegram.bot.command.impl.PublicPhraseSwitcherCommand;
import ua.friends.telegram.bot.command.impl.PunchCommand;
import ua.friends.telegram.bot.command.impl.RatMessageCommand;
import ua.friends.telegram.bot.command.impl.RemovePhraseCommand;
import ua.friends.telegram.bot.command.impl.ShowUserPhrasesCommand;
import ua.friends.telegram.bot.command.impl.UserIdCommand;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreator;
import ua.friends.telegram.bot.cron.gaycron.GayJobCreatorImpl;
import ua.friends.telegram.bot.dao.ChatDao;
import ua.friends.telegram.bot.dao.ChatDaoImpl;
import ua.friends.telegram.bot.dao.CronInfoDao;
import ua.friends.telegram.bot.dao.CronInfoDaoImpl;
import ua.friends.telegram.bot.dao.GayGameDao;
import ua.friends.telegram.bot.dao.GayGameDaoImpl;
import ua.friends.telegram.bot.dao.PhraseDao;
import ua.friends.telegram.bot.dao.PhraseDaoImpl;
import ua.friends.telegram.bot.dao.UserDao;
import ua.friends.telegram.bot.dao.UserDaoImpl;
import ua.friends.telegram.bot.dao.UserToChatDao;
import ua.friends.telegram.bot.dao.UserToChatDaoImpl;
import ua.friends.telegram.bot.service.ChatService;
import ua.friends.telegram.bot.service.ChatServiceImpl;
import ua.friends.telegram.bot.service.CronInfoService;
import ua.friends.telegram.bot.service.CronInfoServiceImpl;
import ua.friends.telegram.bot.service.GayGameService;
import ua.friends.telegram.bot.service.GayGameServiceImpl;
import ua.friends.telegram.bot.service.PhraseService;
import ua.friends.telegram.bot.service.PhraseServiceImpl;
import ua.friends.telegram.bot.service.UserService;
import ua.friends.telegram.bot.service.UserServiceImpl;
import ua.friends.telegram.bot.service.UserToChatService;
import ua.friends.telegram.bot.service.UserToChatServiceImpl;

public class GuiceDIConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(ChatDao.class).to(ChatDaoImpl.class).in(Scopes.SINGLETON);
        bind(UserDao.class).to(UserDaoImpl.class).in(Scopes.SINGLETON);
        bind(UserToChatDao.class).to(UserToChatDaoImpl.class).in(Scopes.SINGLETON);
        bind(PhraseDao.class).to(PhraseDaoImpl.class).in(Scopes.SINGLETON);
        bind(GayGameDao.class).to(GayGameDaoImpl.class).in(Scopes.SINGLETON);
        bind(CronInfoDao.class).to(CronInfoDaoImpl.class).in(Scopes.SINGLETON);

        bind(ChatService.class).to(ChatServiceImpl.class).in(Scopes.SINGLETON);
        bind(UserToChatService.class).to(UserToChatServiceImpl.class).in(Scopes.SINGLETON);
        bind(CronInfoService.class).to(CronInfoServiceImpl.class).in(Scopes.SINGLETON);
        bind(GayGameService.class).to(GayGameServiceImpl.class).in(Scopes.SINGLETON);
        bind(PhraseService.class).to(PhraseServiceImpl.class).in(Scopes.SINGLETON);
        bind(UserService.class).to(UserServiceImpl.class).in(Scopes.SINGLETON);

        bind(Command.class).annotatedWith(Names.named("glashatay")).to(GlashatayCommand.class);
        bind(Command.class).annotatedWith(Names.named("invalidMessage")).to(InvalidMessageCommand.class);
        bind(Command.class).annotatedWith(Names.named("deleteMessage")).to(DeleteMessageCommand.class);
        bind(Command.class).annotatedWith(Names.named("gayReg")).to(GayRegCommand.class);
        bind(Command.class).annotatedWith(Names.named("gayRem")).to(GayRemoveCommand.class);
        bind(Command.class).annotatedWith(Names.named("ban")).to(BanCommand.class);
        bind(Command.class).annotatedWith(Names.named("punch")).to(PunchCommand.class);
        bind(Command.class).annotatedWith(Names.named("userId")).to(UserIdCommand.class);
        bind(Command.class).annotatedWith(Names.named("ratMessage")).to(RatMessageCommand.class);
        bind(Command.class).annotatedWith(Names.named("gayStat")).to(GayStatCommand.class);
        bind(Command.class).annotatedWith(Names.named("gayChoose")).to(GayChooseCommand.class);
        bind(Command.class).annotatedWith(Names.named("addPhrase")).to(AddPhraseCommand.class);
        bind(Command.class).annotatedWith(Names.named("showUserPhrases")).to(ShowUserPhrasesCommand.class);
        bind(Command.class).annotatedWith(Names.named("removePhrases")).to(RemovePhraseCommand.class);
        bind(Command.class).annotatedWith(Names.named("importGayGameStats")).to(ImportGayGameStatsCommand.class);
        bind(Command.class).annotatedWith(Names.named("fullGayGameStats")).to(FullGayGameStatsCommand.class);
        bind(Command.class).annotatedWith(Names.named("phrasePreview")).to(PhrasePreviewCommand.class);
        bind(Command.class).annotatedWith(Names.named("autoPick")).to(AutoPickCommand.class);
        bind(Command.class).annotatedWith(Names.named("publicPhrases")).to(PublicPhraseSwitcherCommand.class);
        bind(Command.class).annotatedWith(Names.named("faq")).to(FAQCommand.class);
        bind(Command.class).annotatedWith(Names.named("response")).to(AdminResponseCommand.class);

        bind(TextCommandExecutor.class).to(TextCommandExecutorImpl.class).in(Scopes.SINGLETON);

        bind(GlashatayBot.class).to(GlashatayBotImpl.class).in(Scopes.SINGLETON);
        bind(GayJobCreator.class).to(GayJobCreatorImpl.class).in(Scopes.SINGLETON);
    }
}
