package java8Concurrency.partTwo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static java8Concurrency.partTwo.ConcurrentUtils.stop;

/**
 *  This class covers the Synchronized portion of part 2 for the Java 8 Concurrency tutorial
 */
public class SynchronizedExample {

    static int count = 0;
    static int count2 = 0;

    /**
     * This method implements some synchronized basics using executors and synchronized increments
     * Synchronized can be used to implicitly lock and unlock threads
     * @param args
     */
    public static void main(String args[]) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Without synchronized we get unexpected and incorrect values
        IntStream.range(0, 10000)
                .forEach(i -> executor.submit(SynchronizedExample::increment));

        stop(executor);

        System.out.println(count);


        ExecutorService executor2 = Executors.newFixedThreadPool(2);

        // using a synchronized method, the values cannot be read at the same time thus giving us the correct result
        IntStream.range(0, 10000)
                .forEach(i -> executor2.submit(SynchronizedExample::incrementSync));

        stop(executor2);

        System.out.println(count2);

    }


    /**
     * basic increment method
     */
    static void increment() {
        count = count + 1;
    }

    /**
     * increment method that implements synchronized
     */
    static synchronized void incrementSync() {
        count2 = count2 + 1;
    }
}
