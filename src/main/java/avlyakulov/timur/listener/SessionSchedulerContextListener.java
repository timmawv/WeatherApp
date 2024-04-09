package avlyakulov.timur.listener;

import avlyakulov.timur.util.scheduler.SessionScheduler;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@WebListener
public class SessionSchedulerContextListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            scheduler = Executors.newScheduledThreadPool(1);

            Runnable task = new SessionScheduler();

            int initialDelayMinutes = 5;
            int intervalMinutes = 15;
            scheduler.scheduleAtFixedRate(task, initialDelayMinutes, intervalMinutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
                scheduler.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (!scheduler.isTerminated()) {
                    scheduler.shutdownNow();
                }
            }
        }
    }
}