package java8Concurrency.partOne;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *  This class covers the Scheduled Executors portion of part 1 for the Java 8 Concurrency tutorial
 */
public class ScheduledExecutors {

    /**
     * This method implements the basics of scheduled executors which are used to periodically run common tasks many
     * times
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {

        System.out.println("\n----------------------------");
        System.out.println("Scheduled Executors");
        System.out.println("----------------------------");



        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // task runs continuously after 3 second delay

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1337);

        // waits 1337 ms then returns how much longer the delay will last
        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms", remainingDelay);

        Thread.sleep(1000);
        System.out.println("\n----------------------------");
        System.out.println("Next Section");
        System.out.println("----------------------------");
        Thread.sleep(1000);


        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);

        Runnable task2 = () -> System.out.println("Scheduling: " + System.nanoTime());

        // runs task every 1 second
        int initialDelay = 0;
        int period = 1;
        executor2.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);


        Thread.sleep(5000);
        System.out.println("----------------------------");
        System.out.println("Next Section");
        System.out.println("----------------------------");
        executor2.shutdownNow();

        ScheduledExecutorService executor3 = Executors.newScheduledThreadPool(1);

        // waits one second after a task ends and one second before next task starts
        Runnable task3 = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            } catch (InterruptedException e) {
                System.out.println("task interrupted");
            }
        };

        executor3.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

    }
}
