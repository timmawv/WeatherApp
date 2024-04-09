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
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = new SessionScheduler();

        scheduler.scheduleAtFixedRate(task, 5, 15, TimeUnit.MINUTES);
    }
}