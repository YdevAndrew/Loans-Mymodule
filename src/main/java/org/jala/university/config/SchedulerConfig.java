package org.jala.university.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * This class provides a singleton instance of a
 * {@link ThreadPoolTaskScheduler} configured for scheduling tasks
 * related to loans.
 */
public class SchedulerConfig {

    /**
     * The singleton instance of the {@link ThreadPoolTaskScheduler}.
     */
    private static final ThreadPoolTaskScheduler SCHEDULER =
            new ThreadPoolTaskScheduler();

    // Define the pool size for the task scheduler
    private static final int SCHEDULER_POOL_SIZE = 5;

    static {
        SCHEDULER.setPoolSize(SCHEDULER_POOL_SIZE);
        SCHEDULER.setThreadNamePrefix("LoanScheduler-");
        SCHEDULER.initialize();
    }

    /**
     * Returns the singleton instance of the
     * {@link ThreadPoolTaskScheduler}.
     *
     * @return The configured task scheduler.
     */
    public static ThreadPoolTaskScheduler getScheduler() {
        return SCHEDULER;
    }

    // Private constructor to prevent instantiation
    private SchedulerConfig() {
    }
}