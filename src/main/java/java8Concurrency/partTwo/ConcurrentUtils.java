package java8Concurrency.partTwo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This is a helper class that implements a stop and sleep method used for the executors in these examples
 * @author Benjamin Winterberg
 */
public class ConcurrentUtils {

    /**
     * used to safely shutdown an executor
     * @param executor
     */
    public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    /**
     * used to safely perform a timeout
     * @param seconds
     */
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}