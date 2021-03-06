package ua.friends.telegram.bot.cron.gaycron;

import javax.inject.Inject;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import ua.friends.telegram.bot.GlashatayBot;
import ua.friends.telegram.bot.GlashatayBotImpl;

public class GayJobCreatorImpl implements GayJobCreator{

    public static final String GAY_JOB = "gayJob";
    public static final String GROUP = "group1";
    public static final String TRIGGER = "trigger";
    public static final String CRON_EXPRESSION = "0 50 23 ? * * *";

    private Scheduler scheduler;

    @Inject
    private GlashatayBot glashatayBot;

    public void schedule() {
        try {

            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(createJobDetail(this.glashatayBot), createTrigger());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private JobDetail createJobDetail(GlashatayBot bot) {
        JobDataMap map = new JobDataMap();
        map.put("bot", bot);
        JobDetail job = JobBuilder.newJob(GayJob.class)
                .setJobData(map)
                .withIdentity(GAY_JOB, GROUP)
                .build();
        return job;
    }

    private final Trigger createTrigger() {
        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity(TRIGGER, GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION)).build();
        return trigger2;
    }
}

