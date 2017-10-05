package java8Concurrency.partThree;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java8Concurrency.partTwo.ConcurrentUtils.stop;

/**
 *  This class covers the AtomicInteger portion of part 3 for the Java 8 Concurrency tutorial
 */
public class AtomicIntegerExample {

    /**
     * this class implements the basics of an AtomicIntgeger. An atomic integer is used when you want to safely alter
     * an integer concurrently with multiple threads without having to worry about using synchronized or locks.
     * @param args
     */
    public static void main(String args[]) {
        AtomicInteger atomicInt = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // using atomic integer method to perform what was done in the synchronized example
        IntStream.range(0, 1000)
                .forEach(i -> executor.submit(atomicInt::incrementAndGet));

        stop(executor);

        System.out.println(atomicInt.get());



        AtomicInteger atomicInt2 = new AtomicInteger(0);

        ExecutorService executor2 = Executors.newFixedThreadPool(2);

        // showing that you can do more than just increment an atomic int by 1
        IntStream.range(0, 1000)
                .forEach(i -> {
                    Runnable task = () ->
                            atomicInt2.updateAndGet(n -> n + 2);
                    executor2.submit(task);
                });

        stop(executor2);

        System.out.println(atomicInt2.get());

        AtomicInteger atomicInt3 = new AtomicInteger(0);

        ExecutorService executor3 = Executors.newFixedThreadPool(2);

        // sums all values from 0 to 1000 using 2 threads and an atomic int
        IntStream.range(0, 1000)
                .forEach(i -> {
                    Runnable task = () ->
                            atomicInt3.accumulateAndGet(i, (n, m) -> n + m);
                    executor3.submit(task);
                });

        stop(executor3);

        System.out.println(atomicInt3.get());
    }
}
