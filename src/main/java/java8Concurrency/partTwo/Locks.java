package java8Concurrency.partTwo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

import static java8Concurrency.partTwo.ConcurrentUtils.*;

/**
 *  This class covers the Locks portion of part 2 for the Java 8 Concurrency tutorial
 */
public class Locks {

    static int count = 0;

    /**
     * This method implements basic lock classes which include ReentrantLock, ReadWriteLock, and StampedLock
     * Locks are used to explicitly lock and unlock threads whereas synchronized does it implicitly
     *
     * @param args
     */
    public static void main(String args[]) {

        System.out.println("----------------------------");
        System.out.println("ReentrantLock");
        System.out.println("----------------------------");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();

        executor.submit(() -> {
            lock.lock();
            try {
                sleep(1);
            } finally {
                lock.unlock();
            }
        });

        // task that shows what it looks like when another thread tries to access a locked task
        executor.submit(() -> {
            System.out.println("Locked: " + lock.isLocked());
            System.out.println("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            System.out.println("Lock acquired: " + locked);
        });

        stop(executor);


        System.out.println("----------------------------");
        System.out.println("Next Section - ReadWriteLock");
        System.out.println("----------------------------");

        ExecutorService executor2 = Executors.newFixedThreadPool(2);
        Map<String, String> map = new HashMap<>();
        ReadWriteLock rwLock = new ReentrantReadWriteLock();

        // write lock only applies when writing to a variable which cannot be done concurrently
        executor2.submit(() -> {
            rwLock.writeLock().lock();
            try {
                sleep(1);
                map.put("foo", "bar");
            } finally {
                rwLock.writeLock().unlock();
            }
        });

        // applies read lock but can still be read concurrently
        Runnable readTask = () -> {
            rwLock.readLock().lock();
            try {
                System.out.println(map.get("foo"));
                sleep(1);
            } finally {
                rwLock.readLock().unlock();
            }
        };

        executor2.submit(readTask);
        executor2.submit(readTask);

        stop(executor2);

        System.out.println("----------------------------");
        System.out.println("Next Section - StampedLock");
        System.out.println("----------------------------");

        ExecutorService executor3 = Executors.newFixedThreadPool(2);
        Map<String, String> map2 = new HashMap<>();
        StampedLock stLock = new StampedLock();

        // same as readwrite lock but uses returned stamp to unlock
        executor3.submit(() -> {
            long stamp = stLock.writeLock();
            try {
                sleep(1);
                map2.put("foo", "bar");
            } finally {
                stLock.unlockWrite(stamp);
            }
        });

        Runnable readTask2 = () -> {
            long stamp = stLock.readLock();
            try {
                System.out.println(map2.get("foo"));
                sleep(1);
            } finally {
                stLock.unlockRead(stamp);
            }
        };

        executor3.submit(readTask2);
        executor3.submit(readTask2);

        stop(executor3);



        ExecutorService executor4 = Executors.newFixedThreadPool(2);
        StampedLock stLock2 = new StampedLock();

        // task that checks if locks are still valid using stamps
        executor4.submit(() -> {
            long stamp = stLock2.tryOptimisticRead();
            try {
                System.out.println("Optimistic Lock Valid: " + stLock2.validate(stamp));
                sleep(1);
                System.out.println("Optimistic Lock Valid: " + stLock2.validate(stamp));
                sleep(2);
                System.out.println("Optimistic Lock Valid: " + stLock2.validate(stamp));
            } finally {
                stLock2.unlock(stamp);
            }
        });

        executor4.submit(() -> {
            long stamp = stLock2.writeLock();
            try {
                System.out.println("Write Lock acquired");
                sleep(2);
            } finally {
                stLock2.unlock(stamp);
                System.out.println("Write done");
            }
        });

        stop(executor4);

        ExecutorService executor5 = Executors.newFixedThreadPool(2);
        StampedLock stLock3 = new StampedLock();

        // task that converts a read lock to a write lock using a stamp
        executor5.submit(() -> {
            long stamp = stLock3.readLock();
            try {
                if (count == 0) {
                    stamp = stLock3.tryConvertToWriteLock(stamp);
                    if (stamp == 0L) {
                        System.out.println("Could not convert to write lock");
                        stamp = stLock3.writeLock();
                    }
                    count = 23;
                }
                System.out.println(count);
            } finally {
                stLock3.unlock(stamp);
            }
        });

        stop(executor5);

    }

    /**
     * Simple increment method that locks a thread, increments count by 1, then unlocks it. This prevents two threads
     * from incrementing the same count value
     */
    void increment() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

}
