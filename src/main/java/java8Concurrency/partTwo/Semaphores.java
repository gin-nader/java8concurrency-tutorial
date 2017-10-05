package java8Concurrency.partTwo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java8Concurrency.partTwo.ConcurrentUtils.sleep;
import static java8Concurrency.partTwo.ConcurrentUtils.stop;

/**
 *  This class covers the Semaphores portion of part 2 for the Java 8 Concurrency tutorial
 */
public class Semaphores {

    /**
     * This method implements some semaphore basics. A semaphore is like a lock but it can be used to limit how many
     * people can access a resource at one time
     * @param args
     */
    public static void main(String args[]) {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        Semaphore semaphore = new Semaphore(5);

        // we can have 10 threads accessing our task at once but we are using a semaphore to limit that access to 5
        // threads at a time
        Runnable longRunningTask = () -> {
            boolean permit = false;
            try {
                permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
                if (permit) {
                    System.out.println("Semaphore acquired");
                    sleep(5);
                } else {
                    System.out.println("Could not acquire semaphore");
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            } finally {
                if (permit) {
                    semaphore.release();
                }
            }
        };

        IntStream.range(0, 10)
                .forEach(i -> executor.submit(longRunningTask));

        stop(executor);
    }


}
