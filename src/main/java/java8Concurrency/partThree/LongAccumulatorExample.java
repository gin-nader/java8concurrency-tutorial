package java8Concurrency.partThree;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

import static java8Concurrency.partTwo.ConcurrentUtils.stop;

/**
 *  This class covers the LongAccumulator portion of part 3 for the Java 8 Concurrency tutorial
 */
public class LongAccumulatorExample {

    /**
     * This method implements a basic example of the LongAccumulator class. It is similar to LongAdder but it used to
     * perform a lambda expression in a thread rather than just adding. It uses a LongBinaryOperator to store the
     * expression
     * @param args
     */
    public static void main(String args[]) {
        LongBinaryOperator op = (x, y) -> 2 * x + y;
        LongAccumulator accumulator = new LongAccumulator(op, 1L);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // uses initial value of 1 then performs operation on a range of 0 to 10
        IntStream.range(0, 10)
                .forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

        stop(executor);

        System.out.println(accumulator.getThenReset());
    }
}
