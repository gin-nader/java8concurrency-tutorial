package java8Concurrency.partThree;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import static java8Concurrency.partTwo.ConcurrentUtils.stop;

/**
 *  This class covers the LongAdder portion of part 3 for the Java 8 Concurrency tutorial
 */
public class LongAdderExample {

    /**
     * This method implements the basics of a LongAdder. It is like an atomic long but has internal variables to reduce
     * contention over threads. They are usually preferred when you are doing more concurrent updates than reads.
     * @param args
     */
    public static void main(String args[]) {

        LongAdder adder = new LongAdder();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(i -> executor.submit(adder::increment));

        stop(executor);

        System.out.println(adder.sumThenReset());
    }
}
