package avlyakulov.timur.util.scheduler;

import avlyakulov.timur.dao.SessionDao;

public class SessionScheduler implements Runnable {

    private final SessionDao sessionDao = new SessionDao();

    @Override
    public void run() {
        sessionDao.deleteExpiredSessions();
    }
}