package java8Concurrency.partOne;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 *  This class covers the Executors portion of part 1 for the Java 8 Concurrency tutorial
 */
public class ExecutorsExample {

    /**
     * This method uses the basics of executors which includes callables and futures, timeouts, invoke all, and invoke
     * any.
     *
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static void main(String args[]) throws ExecutionException, InterruptedException, TimeoutException {

        System.out.println("----------------------------");
        System.out.println("Executors");
        System.out.println("----------------------------");

        // Executors utilize a pool of threads

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);

        });

        // Shutting down an executor safely

        try {
            Thread.sleep(1000);
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }


        System.out.println("----------------------------");
        System.out.println("Next Section - Callables and Futures");
        System.out.println("----------------------------");

        // Callables are like runnables, but they return a value called a future.

        Callable<Integer> task = () -> {
            try {
                TimeUnit.SECONDS.sleep(1)   ;
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        // Futures can be used to retrieve a result at a later time

        executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        Integer result = future.get();

        System.out.println("future done? " + future.isDone());
        System.out.println("result: " + result);

        executor.shutdownNow();
        future.get();

        System.out.println("----------------------------");
        System.out.println("Next Section - Timeouts");
        System.out.println("----------------------------");

        // Timeouts can be used to specify and amount of time to wait before retrieving the result

        executor = Executors.newFixedThreadPool(1);

        future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        // Exception is thrown because we are waiting 1 second, but the future takes 2 seconds to submit

        //future.get(1, TimeUnit.SECONDS);

        future.get(2, TimeUnit.SECONDS);

        System.out.println("----------------------------");
        System.out.println("Next Section - InvokeAll");
        System.out.println("----------------------------");

        // Invoke all can batch submit multiple callables at once

        executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");


        // takes three tasks and returns a list of futures
        executor.invokeAll(callables)
                .stream()
                .map(future2 -> {
                    try {
                        return future2.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);

        System.out.println("----------------------------");
        System.out.println("Next Section - InvokeAny");
        System.out.println("----------------------------");



        executor = Executors.newWorkStealingPool();

        callables = Arrays.asList(
                callable("task1", 2),
                callable("task2", 4),
                callable("task3", 3));

        // takes three tasks and returns the one the completes first

        String resultStr = executor.invokeAny(callables);
        System.out.println(resultStr);





    }

    /**
     * helper method so we can give our task a specified timeout
     *
     * @param result task name
     * @param sleepSeconds length of timeout
     * @return the task
     */
    static Callable<String> callable(String result, long sleepSeconds) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }
}
