package java8Concurrency.partOne;

import java.util.concurrent.TimeUnit;

/**
 *  This class covers the Threads and Runnables portion of part 1 for the Java 8 Concurrency tutorial
 */
public class ThreadsAndRunnables {

    /**
     * This method implements threads and runnables and uses them to print messages in parallel
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {

        System.out.println("----------------------------");
        System.out.println("Threads");
        System.out.println("----------------------------");

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        // uses main thread to print name of thread
        task.run();

        // spawns a second thread to print name of thread
        Thread thread = new Thread(task);
        thread.start();

        System.out.println("Done!");

        // keeps sections separated
        Thread.sleep(1000);
        System.out.println("----------------------------");
        System.out.println("Next Section");
        System.out.println("----------------------------");

        Runnable runnable = () -> {
            try {
                String name = Thread.currentThread().getName();
                System.out.println("Foo " + name);
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + name);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        };

        Thread thread2 = new Thread(runnable);
        thread2.start();


    }
}
