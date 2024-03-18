package avlyakulov.timur.scheduler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PostLoad;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionCleanupScheduler {

//    //todo make work scheduler
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//    @PostLoad
//    public void init() {
//        scheduler.scheduleAtFixedRate(this::cleanupExpiredDates, 0, 1, TimeUnit.MINUTES);
//    }
//
//    @Transactional
//    public void cleanupExpiredDates() {
//        try {
//            LocalDateTime currentDateTime = LocalDateTime.now();
//
//            int deletedEntities = entityManager.createQuery("DELETE FROM Session WHERE expiresAt < :currentDateTime")
//                    .setParameter("currentDateTime", currentDateTime)
//                    .executeUpdate();
//
//            System.out.println(deletedEntities + " records were deleted.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}